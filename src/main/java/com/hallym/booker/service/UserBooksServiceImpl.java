package com.hallym.booker.service;

import com.hallym.booker.controller.ApiTagValue;
import com.hallym.booker.domain.BookDetails;
import com.hallym.booker.domain.Profile;
import com.hallym.booker.domain.UserBooks;
import com.hallym.booker.dto.Profile.ProfileResponseDTO;
import com.hallym.booker.dto.UserBooks.*;
import com.hallym.booker.exception.profile.NoSuchProfileException;
import com.hallym.booker.exception.userBooks.DuplicateUserBooksException;
import com.hallym.booker.exception.userBooks.NoSuchUserBooksException;
import com.hallym.booker.repository.BookDetailsRepository;
import com.hallym.booker.repository.ProfileRepository;
import com.hallym.booker.repository.UserBooksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserBooksServiceImpl implements UserBooksService {

    private final UserBooksRepository userBooksRepository;
    private final BookDetailsRepository bookDetailsRepository;
    private final ProfileRepository profileRepository;
    private final ApiTagValue apiTagValue;

    @Value("${aladin.api.key}")
    private String apiKey;

    private static final String ALADIN_API_URL1 = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx?ttbkey=%s&Sort=SalesPoint&Query=%s&QueryType=Keyword&start=1&SearchTarget=Book&output=xml&Version=20131101&Cover=Big";

    // 책 등록
    @Override
    public void saveUserBooks(UserBooksDTO userBooksDTO) {
        Profile profile = profileRepository.findById(userBooksDTO.getProfileUid())
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
    public UserBooksReadStatusResponseDTO getReadStatus(Long profileUid, String isbn) {
        Profile profile = profileRepository.findById(profileUid)
                .orElseThrow(() -> new NoSuchProfileException());

        UserBooks userBooks = userBooksRepository.findByProfileUidAndIsbn(profile, isbn);
        if (userBooks == null) {
            throw new NoSuchUserBooksException();
        }

        UserBooksReadStatusResponseDTO responseDTO = new UserBooksReadStatusResponseDTO();
        responseDTO.setBookUid(userBooks.getBookUid());
        responseDTO.setReadStatus(userBooks.getReadStatus());
        responseDTO.setSaleStatus(userBooks.getSaleStatus());

        return responseDTO;
    }

    // 책 검색에서 isbn을 통해 독서 상태 및 책(알라딘) 조회
    @Override
    public List<BooksWithStatusDTO> searchBooks(Long profileUid, String searchOne) {
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
            Profile profile = profileRepository.findById(profileUid)
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
}
