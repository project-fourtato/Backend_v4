package com.hallym.booker.service;

import com.hallym.booker.domain.Profile;
import com.hallym.booker.domain.UserBooks;
import com.hallym.booker.dto.ReadingAllBooksListResponse;
import com.hallym.booker.dto.ReadingBookResponse;
import com.hallym.booker.exception.profile.NoSuchProfileException;
import com.hallym.booker.repository.BookDetailsRepository;
import com.hallym.booker.repository.ProfileRepository;
import com.hallym.booker.repository.UserBooksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserBooksService {
    private final UserBooksRepository userBooksRepository;
    private final BookDetailsRepository bookDetailsRepository;
    private final ProfileRepository profileRepository;

    /**
     * 읽고 있는 책 목록
     */
    public ReadingAllBooksListResponse readingAllBooksList(Long profileId) {
        Profile profile = profileRepository.findById(profileId).orElseThrow(NoSuchProfileException::new);

        List<UserBooks> allByProfile = userBooksRepository.findAllByProfile(profile);
        return ReadingAllBooksListResponse.from(allByProfile);
    }

    /**
     * 책을 같이 읽는 유저 목록
     */
    public ReadingWithAllProfileList readingWithProfileList(Long profileId) {
        List<UserBooks> withProfileList = userBooksRepository.findWithProfileList(profileId);

        Map<BookDetails, List<Profile>> map = new ConcurrentHashMap<>();
        for (UserBooks userBooks : withProfileList) {
            if(map.containsKey(userBooks.getBookDetails())) {
                List<Profile> profileList = map.get(userBooks.getBookDetails());
                profileList.add(userBooks.getProfile());

                map.put(userBooks.getBookDetails(), profileList);
            } else {
                List<Profile> profileList = new ArrayList<>();
                profileList.add(userBooks.getProfile());

                map.put(userBooks.getBookDetails(), profileList);
            }
        }

        return ReadingWithAllProfileList.from(map);
    }

    /**
     * 베스트셀러
     */
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
    public String getTagValue(String tag, Element eElement) {

        //결과를 저장할 result 변수 선언
        String result = "";

        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();

        result = nlList.item(0).getTextContent();

        return result;
    }
}
