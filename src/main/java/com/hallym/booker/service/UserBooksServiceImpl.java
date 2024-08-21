package com.hallym.booker.service;

import com.hallym.booker.controller.ApiTagValue;
import com.hallym.booker.domain.BookDetails;
import com.hallym.booker.domain.Profile;
import com.hallym.booker.domain.UserBooks;
import com.hallym.booker.dto.Profile.ProfileResponseDTO;
import com.hallym.booker.dto.UserBooks.*;
import com.hallym.booker.exception.profile.NoSuchLoginException;
import com.hallym.booker.exception.profile.NoSuchProfileException;
import com.hallym.booker.exception.userBooks.DuplicateUserBooksException;
import com.hallym.booker.exception.userBooks.NoSuchUserBooksException;
import com.hallym.booker.repository.BookDetailsRepository;
import com.hallym.booker.repository.LoginRepository;
import com.hallym.booker.repository.ProfileRepository;
import com.hallym.booker.repository.UserBooksRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserBooksServiceImpl implements UserBooksService {

    private final LoginRepository loginRepository;
    private final UserBooksRepository userBooksRepository;
    private final BookDetailsRepository bookDetailsRepository;
    private final ProfileRepository profileRepository;
    private final ApiTagValue apiTagValue;

    @Value("${aladin.api.key}")
    private String apiKey;

    private static final String ALADIN_API_URL1 = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx?ttbkey=%s&Sort=SalesPoint&Query=%s&QueryType=Keyword&start=1&SearchTarget=Book&output=xml&Version=20131101&Cover=Big";

    // 책 등록
    @Override
    public void saveUserBooks(String loginUid, UserBooksDTO userBooksDTO) {
        Profile profile = profileRepository.findById(loginRepository.findById(loginUid).get().getProfile().getProfileUid())
                .orElseThrow(() -> new NoSuchProfileException());

        BookDetails bookDetails = bookDetailsRepository.findByIsbn(userBooksDTO.getIsbn());

        if (bookDetails == null) {
            bookDetails = BookDetails.create(
                    userBooksDTO.getIsbn(),
                    userBooksDTO.getBookTitle(),
                    userBooksDTO.getAuthor(),
                    userBooksDTO.getPublisher(),
                    userBooksDTO.getCoverImageUrl()
            );
            bookDetailsRepository.save(bookDetails);
        }

        List<UserBooks> existingUserBooks = userBooksRepository.findByProfileAndBookDetails(profile, bookDetails);
        if (!existingUserBooks.isEmpty()) {
            throw new DuplicateUserBooksException();
        }

        UserBooks userBooks = UserBooks.create(profile, bookDetails, userBooksDTO.getReadStatus(), userBooksDTO.getSaleStatus());
        userBooksRepository.save(userBooks);
    }


    // 독서 상태 변경
    @Override
    public String updateReadStatus(Long bookUid, UserBooksUpdateRequestDTO updateDTO) {
        UserBooks userBooks = userBooksRepository.findById(bookUid)
                .orElseThrow(() -> new NoSuchUserBooksException());

        userBooks.change(updateDTO.getReadStatus(), userBooks.getSaleStatus());
        userBooksRepository.save(userBooks);

        return "독서 상태 변경 성공";
    }

    // 책 판매 상태 변경
    @Override
    public String updateSaleStatus(Long bookUid, UserBooksUpdateRequestDTO updateDTO) {
        UserBooks userBooks = userBooksRepository.findById(bookUid)
                .orElseThrow(() -> new NoSuchUserBooksException());

        userBooks.change(userBooks.getReadStatus(), updateDTO.getSaleStatus());
        userBooksRepository.save(userBooks);

        return "판매 상태 변경 성공";
    }

    // 한 책에 대한 독서 상태 조회
    @Override
    public UserBooksReadStatusResponseDTO getReadStatus(String loginUid, String isbn) {
        Profile profile = profileRepository.findById(
                        loginRepository.findById(loginUid).get().getProfile().getProfileUid())
                .orElseThrow(() -> new NoSuchProfileException());

        UserBooks userBooks = userBooksRepository.findByProfileUidAndIsbn(profile, isbn);
        if (userBooks == null) {
            return new UserBooksReadStatusResponseDTO(null, -1, -1);
        }

        UserBooksReadStatusResponseDTO responseDTO = new UserBooksReadStatusResponseDTO();
        responseDTO.setBookUid(userBooks.getBookUid());
        responseDTO.setReadStatus(userBooks.getReadStatus());
        responseDTO.setSaleStatus(userBooks.getSaleStatus());

        return responseDTO;
    }

    // 책 검색에서 isbn을 통해 독서 상태 및 책(알라딘) 조회
    @Override
    public List<BooksWithStatusDTO> searchBooks(String loginUid, String searchOne) {
        List<BooksWithStatusDTO> booksList = new ArrayList<>();
        String url = String.format(ALADIN_API_URL1, apiKey, searchOne);

        Document doc = getXmlDocument(url);
        if (doc == null) {
            throw new NoSuchUserBooksException();
        }

        NodeList nList = doc.getElementsByTagName("item");
        if (nList.getLength() == 0) {
            throw new NoSuchUserBooksException();
        }

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Element eElement = (Element) nList.item(temp);

            // 책 정보 추출
            String bookTitle = apiTagValue.getTagValue("title", eElement);
            String author = apiTagValue.getTagValue("author", eElement);
            String isbn = apiTagValue.getTagValue("isbn13", eElement);
            String publisher = apiTagValue.getTagValue("publisher", eElement);
            String coverImageUrl = apiTagValue.getTagValue("cover", eElement);

            BooksWithStatusDTO bookDto = new BooksWithStatusDTO(isbn, bookTitle, author, publisher, coverImageUrl, 0);

            // 프로필 조회
            Profile profile = profileRepository.findById(
                            loginRepository.findById(loginUid).get().getProfile().getProfileUid())
                    .orElseThrow(() -> new NoSuchProfileException());

            // 사용자의 책 상태 조회
            UserBooks userBook = userBooksRepository.findByProfileUidAndIsbn(profile, isbn);
            if (userBook != null) {
                bookDto.setReadStatus(userBook.getReadStatus());
            }

            booksList.add(bookDto);
        }

        return booksList;
    }

    // 책 교환에서 검색된 책 목록 조회
    @Override
    public List<BooksFindDTO> searchBooks(String searchOne) {
        return searchBooksCommon(searchOne);
    }

    // 책 검색 공통 메소드
    private List<BooksFindDTO> searchBooksCommon(String searchOne) {
        List<BooksFindDTO> booksList = new ArrayList<>();
        String encodedQuery = searchOne.replace(" ", "%20");
        String url = String.format(ALADIN_API_URL1, apiKey, encodedQuery);

        Document doc = getXmlDocument(url);
        if (doc == null) {
            throw new NoSuchUserBooksException();
        }

        NodeList nList = doc.getElementsByTagName("item");
        if (nList.getLength() == 0) {
            throw new NoSuchUserBooksException();
        }

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Element eElement = (Element) nList.item(temp);
            BooksFindDTO bookDto = new BooksFindDTO(
                    apiTagValue.getTagValue("title", eElement),
                    apiTagValue.getTagValue("author", eElement),
                    apiTagValue.getTagValue("isbn13", eElement),
                    apiTagValue.getTagValue("publisher", eElement),
                    apiTagValue.getTagValue("cover", eElement)
            );
            booksList.add(bookDto);
        }

        return booksList;
    }

    // 책 교환에서 isbn과 salesstate을 이용하여 profileUid 추출 후 프로필 목록 조회
    @Override
    public List<ProfileResponseDTO> getProfilesByIsbnAndSaleStatus(String isbn) {
        List<Profile> profiles = userBooksRepository.findByIsbnAndSalesstate(isbn);
        if (profiles.isEmpty()) {
            throw new NoSuchUserBooksException();
        }
        return profiles.stream()
                .map(profile -> new ProfileResponseDTO(
                        profile.getProfileUid(),
                        profile.getNickname(),
                        profile.getUserimageUrl(),
                        profile.getUserimageName(),
                        profile.getUsermessage()))
                .collect(Collectors.toList());
    }

    // XML Document 생성 메소드
    private Document getXmlDocument(String url) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            return dBuilder.parse(url);
        } catch (Exception e) {
            throw new NoSuchUserBooksException();
        }
    }

    /**
     * 읽고 있는 책 목록
     */
    @Override
    public ReadingAllBooksListResponse readingAllBooksList(String loginId) {
        Profile profile = loginRepository.findById(loginId).orElseThrow(NoSuchLoginException::new).getProfile();

        List<UserBooks> allByProfile = userBooksRepository.findAllByProfile(profile);
        return ReadingAllBooksListResponse.from(allByProfile);
    }

    /**
     * 책을 같이 읽는 유저 목록
     */
    @Override
    public ReadingWithAllProfileList readingWithProfileList(String loginId) {
        Profile profile = loginRepository.findById(loginId).orElseThrow(NoSuchLoginException::new).getProfile();
        List<UserBooks> withProfileList = userBooksRepository.findWithProfileList(profile.getProfileUid());

        Map<BookDetails, ReadingProfileWithBookUid> map = new ConcurrentHashMap<>();

        for (UserBooks userBooks : withProfileList) {
            List<ReadingProfile> readingProfile;

            if(map.containsKey(userBooks)) {
                readingProfile = map.get(userBooks.getBookDetails()).getReadingProfile();
            } else {
                readingProfile = new ArrayList<>();
            }

            if(!userBooks.getProfile().getLogin().getLoginUid().equals(loginId)) {
                readingProfile.add(ReadingProfile.of(userBooks.getProfile()));
            }
            map.put(userBooks.getBookDetails(), ReadingProfileWithBookUid.of(readingProfile, userBooks.getBookUid()));
        }

        return ReadingWithAllProfileList.from(map);
    }

    /**
     * 베스트셀러
     */
    @Override
    public BestSellerListResponse bestseller() {
        // 본인이 받은 api키를 추가
        String key = "ttbblossom69842039002";
        List<BestSellerResponse> collect = new ArrayList<>();

        try {
            // parsing할 url 지정(API 키 포함해서)
            String url = "http://www.aladin.co.kr/ttb/api/ItemList.aspx?ttbkey=ttbhyejmh1853001&QueryType=Bestseller&MaxResults=10&start=1&SearchTarget=Book&output=xml&Version=20131101&Cover=Big";

            DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
            Document doc = dBuilder.parse(url);

            // 제일 첫번째 태그
            doc.getDocumentElement().normalize();

            // 파싱할 tag
            NodeList nList = doc.getElementsByTagName("item");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                Element eElement = (Element) nNode;

                BestSellerResponse br = new BestSellerResponse(getTagValue("title", eElement), getTagValue("author", eElement), getTagValue("isbn13", eElement), getTagValue("publisher", eElement),getTagValue("cover", eElement));
                collect.add(br);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new BestSellerListResponse(collect);
    }

    // tag값의 정보를 가져오는 함수
    @Override
    public String getTagValue(String tag, Element eElement) {

        //결과를 저장할 result 변수 선언
        String result = "";

        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();

        result = nlList.item(0).getTextContent();

        return result;
    }

}
