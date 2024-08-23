package com.hallym.booker.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hallym.booker.domain.BookDetails;
import com.hallym.booker.domain.Login;
import com.hallym.booker.domain.Profile;
import com.hallym.booker.domain.UserBooks;
import com.hallym.booker.dto.BookDetails.BookDetailsResponseDTO;
import com.hallym.booker.dto.Profile.ProfileResponseDTO;
import com.hallym.booker.dto.UserBooks.*;
import com.hallym.booker.exception.profile.NoSuchLoginException;
import com.hallym.booker.exception.profile.NoSuchProfileException;
import com.hallym.booker.exception.userBooks.DuplicateUserBooksException;
import com.hallym.booker.exception.userBooks.NoSuchAladinApiIsbnSearchResult;
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
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.*;
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

    @Value("${aladin.api.key}")
    private String apiKey;

    private static final String ALADIN_API_URL1 = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx?ttbkey=%s&Sort=SalesPoint&Query=%s&QueryType=Keyword&start=1&SearchTarget=Book&output=js&Version=20131101&Cover=Big";
    private static final String ALADIN_API_URL2 = "http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx?ttbkey=%s&itemIdType=ISBN13&ItemId=%s&output=js&Version=20131101&Cover=Big";

    // 책 등록
    @Override
    public void saveUserBooks(String loginUid, UserBooksDTO userBooksDTO) {
        Profile profile = profileRepository.findById(loginRepository.findById(loginUid).get().getProfile().getProfileUid())
                .orElseThrow(() -> new NoSuchProfileException());

        Optional<BookDetails> bookDetails = bookDetailsRepository.findByIsbn(userBooksDTO.getIsbn());

        if (!bookDetails.isPresent()) {
            BookFindDTO bookFindDTO = searchBook(userBooksDTO.getIsbn());

            bookDetails = Optional.of(BookDetails.create(
                    bookFindDTO.getIsbn(),
                    bookFindDTO.getBookTitle(),
                    bookFindDTO.getAuthor(),
                    bookFindDTO.getPublisher(),
                    bookFindDTO.getCoverImageUrl()
            ));
            bookDetailsRepository.save(bookDetails.get());
        }

        List<UserBooks> existingUserBooks = userBooksRepository.findByProfileAndBookDetails(profile, bookDetails.get());
        if (!existingUserBooks.isEmpty()) {
            throw new DuplicateUserBooksException();
        }

        UserBooks userBooks = UserBooks.create(profile, bookDetails.get(), userBooksDTO.getReadStatus(), userBooksDTO.getSaleStatus());
        userBooksRepository.save(userBooks);
    }

    private BookFindDTO searchBook(String searchIsbn) {
        String url = String.format(ALADIN_API_URL2, apiKey, searchIsbn);
        List<BookFindDTO> bookFindAllDTO = aladinApiJson(url);

        if(bookFindAllDTO.size() == 0) {
            throw new NoSuchAladinApiIsbnSearchResult();
        }

        return bookFindAllDTO.get(0);
    }

    // 책 삭제
    @Override
    @Transactional
    public String deleteUserBooks(Long bookUid) {
        UserBooks userBooks = userBooksRepository.findById(bookUid)
                .orElseThrow(() -> new NoSuchUserBooksException());

        userBooksRepository.delete(userBooks);

        return "책 삭제 성공";
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
        Profile profile = profileRepository.findById(
                        loginRepository.findById(loginUid).get().getProfile().getProfileUid())
                .orElseThrow(() -> new NoSuchProfileException());
        List<BooksWithStatusDTO> booksList = new ArrayList<>();
        String url = String.format(ALADIN_API_URL1, apiKey, searchOne);

        try {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);

            JsonNode arrayNode = rootNode.get("item");
            for (JsonNode element : arrayNode) {
                BooksWithStatusDTO bookDto = new BooksWithStatusDTO(element.get("author").asText(),
                        element.get("title").asText(),
                        element.get("author").asText(),
                        element.get("publisher").asText(),
                        element.get("cover").asText(), 0);

                UserBooks userBook = userBooksRepository.findByProfileUidAndIsbn(profile, element.get("isbn13").asText());
                if (userBook != null) {
                    bookDto.setReadStatus(userBook.getReadStatus());
                }

                booksList.add(bookDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return booksList;
    }

    // 책 교환에서 검색된 책 목록 조회
    @Override
    public BookFindAllDTO searchBooks(String searchOne) {
        String encodedQuery = searchOne.replace(" ", "%20");
        String url = String.format(ALADIN_API_URL1, apiKey, encodedQuery);

        List<BookFindDTO> bookFindAllDTO = aladinApiJson(url);

        return BookFindAllDTO.of(bookFindAllDTO);
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
                        profile.getLogin().getLoginUid(),
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
    public ReadingWithAllUserList readingWithProfileList(String loginId) {
        Profile profile = loginRepository.findById(loginId).orElseThrow(NoSuchLoginException::new).getProfile();
        List<UserBooks> userBooksList = userBooksRepository.findAllByProfileUid(profile.getProfileUid());
        List<UserBooks> withProfileList = userBooksRepository.findWithProfileListAndFollowList(profile.getProfileUid());

        Map<BookDetails, ReadingProfileWithBookUid> map = new ConcurrentHashMap<>();

        for (UserBooks userBooks : userBooksList) {
            map.put(userBooks.getBookDetails(), ReadingProfileWithBookUid.of(new ArrayList<>(), userBooks.getBookUid()));
        }

        for (UserBooks userBooks : withProfileList) {
            List<ReadingProfile> readingProfile;

            if(map.containsKey(userBooks.getBookDetails())) {
                readingProfile = map.get(userBooks.getBookDetails()).getReadingProfile();
            } else {
                readingProfile = new ArrayList<>();
            }

            if(!userBooks.getProfile().getLogin().getLoginUid().equals(loginId)) {
                readingProfile.add(ReadingProfile.of(userBooks.getProfile()));
            }
            map.put(userBooks.getBookDetails(), ReadingProfileWithBookUid.of(readingProfile, userBooks.getBookUid()));
        }

        return ReadingWithAllUserList.from(map);
    }

    /**
     * 베스트셀러
     */
    @Override
    public BookFindAllDTO bestseller() {
        String url = "http://www.aladin.co.kr/ttb/api/ItemList.aspx?ttbkey=" + apiKey + "&QueryType=Bestseller&MaxResults=10&start=1&SearchTarget=Book&output=js&Version=20131101&Cover=Big";
        List<BookFindDTO> bookFindAllDTO = aladinApiJson(url);

        return BookFindAllDTO.of(bookFindAllDTO);
    }

    private List<BookFindDTO> aladinApiJson(String url) {
        List<BookFindDTO> collect = new ArrayList<>();

        try {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);

            JsonNode arrayNode = rootNode.get("item");
            for (JsonNode element : arrayNode) {
                BookFindDTO bookFindDTO = new BookFindDTO(element.get("title").asText(),
                        element.get("author").asText(),
                        element.get("isbn13").asText(),
                        element.get("publisher").asText(),
                        element.get("cover").asText());

                collect.add(bookFindDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return collect;
    }

}
