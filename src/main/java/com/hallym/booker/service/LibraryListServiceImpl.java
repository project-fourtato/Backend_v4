package com.hallym.booker.service;

import com.hallym.booker.controller.ApiTagValue;
import com.hallym.booker.domain.LibraryList;
import com.hallym.booker.dto.DeduplicationUtils;
import com.hallym.booker.dto.LibraryList.LibraryListDTO;
import com.hallym.booker.exception.libraryList.NoSuchLibraryListException;
import com.hallym.booker.exception.userBooks.NoSuchUserBooksException;
import com.hallym.booker.repository.LibraryListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LibraryListServiceImpl implements LibraryListService {

    private final LibraryListRepository libraryListRepository;
    private final ApiTagValue apiTagValue;

    @Value("${library.api.key}")
    private String apiKey;

    // 도서관 이름으로 검색하여 도서관 코드 리스트 반환
    @Override
    public List<String> searchLibrary(String libName) {
        List<LibraryList> libraries = libraryListRepository.findByName(libName);
        if (libraries.isEmpty()) {
            throw new NoSuchLibraryListException();
        }

        List<String> libraryCodes = new ArrayList<>();
        for (LibraryList lib : libraries) {
            libraryCodes.add(lib.getLibCode());
        }
        return libraryCodes;
    }

    // 지역, 세부 지역, ISBN을 이용하여 해당 책을 소장하는 도서관 목록 조회
    @Override
    public List<LibraryListDTO> findLibrariesByRegionAndISBN(String region, String dtl_region, String isbn) {
        String url = buildUrl("https://data4library.kr/api/libSrchByBook", apiKey, region, dtl_region, isbn);
        List<LibraryListDTO> libraries = fetchLibraries(url);

        if (libraries.isEmpty()) {
            throw new NoSuchUserBooksException();
        }

        return libraries;
    }

    // 지역, 세부 지역, 검색어를 이용하여 도서관 목록 조회
    @Override
    public List<LibraryListDTO> librarySearch(String region, String dtl_region, String searchOne) {
        String baseUrl = buildUrl("http://data4library.kr/api/libSrch", apiKey, region, dtl_region, null);
        List<LibraryListDTO> libraries = new ArrayList<>();

        // 검색어가 비어있지 않으면 해당 도서관 코드로 검색하여 목록을 추가
        if (!searchOne.isEmpty()) {
            List<String> libraryCodes = searchLibrary(searchOne);
            for (String code : libraryCodes) {
                libraries.addAll(fetchLibraries(baseUrl + "&libCode=" + urlEncode(code)));
            }
        } else {
            // 검색어가 비어있으면 기본 URL로 도서관 목록을 조회
            libraries.addAll(fetchLibraries(baseUrl));
        }

        if (libraries.isEmpty()) {
            throw new NoSuchLibraryListException();
        }

        // 중복 제거 후 반환
        return DeduplicationUtils.deduplication(libraries, LibraryListDTO::getLibName);
    }

    // 주어진 URL을 통해 도서관 목록 조회
    @Override
    public List<LibraryListDTO> searchLibraries(String url) {
        List<LibraryListDTO> libraries = fetchLibraries(url);

        if (libraries.isEmpty()) {
            throw new NoSuchLibraryListException();
        }

        return libraries;
    }

    // API 호출을 위한 URL 생성
    private String buildUrl(String baseUrl, String authKey, String region, String dtl_region, String isbn) {
        StringBuilder urlBuilder = new StringBuilder(baseUrl)
                .append("?authKey=").append(urlEncode(authKey));

        // 지역, 세부 지역, ISBN 파라미터 추가
        appendIfNotEmpty(urlBuilder, "region", region);
        appendIfNotEmpty(urlBuilder, "dtl_region", dtl_region);
        appendIfNotEmpty(urlBuilder, "isbn", isbn);

        return urlBuilder.toString();
    }

    // URL 파라미터가 비어있지 않으면 추가하는 메소드
    private void appendIfNotEmpty(StringBuilder urlBuilder, String param, String value) {
        if (value != null && !value.isEmpty()) {
            urlBuilder.append("&").append(param).append("=").append(urlEncode(value));
        }
    }

    // URL 인코딩
    private String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            throw new RuntimeException("URL 매개변수를 인코딩하지 못했습니다: " + value, e);
        }
    }

    // 주어진 URL을 통해 XML 데이터를 파싱하여 도서관 목록을 가져옴
    private List<LibraryListDTO> fetchLibraries(String url) {
        Document document = parseXmlFromUrl(url);
        NodeList nodeList = document.getElementsByTagName("lib");

        if (nodeList.getLength() == 0) {
            throw new NoSuchLibraryListException();
        }

        List<LibraryListDTO> libraryList = new ArrayList<>();
        // XML에서 도서관 정보 추출하여 DTO 객체로 변환 후 리스트에 추가
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                LibraryListDTO library = new LibraryListDTO(
                        apiTagValue.getTagValue("libName", element),
                        apiTagValue.getTagValue("address", element),
                        apiTagValue.getTagValue("tel", element),
                        apiTagValue.getTagValue("latitude", element),
                        apiTagValue.getTagValue("longitude", element),
                        apiTagValue.getTagValue("homepage", element),
                        apiTagValue.getTagValue("closed", element),
                        apiTagValue.getTagValue("operatingTime", element)
                );
                libraryList.add(library);
            }
        }
        return libraryList;
    }

    // 주어진 URL에서 XML 데이터를 파싱하여 Document 객체로 반환
    private Document parseXmlFromUrl(String url) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(url);
        } catch (Exception e) {
            throw new NoSuchLibraryListException();
        }
    }
}
