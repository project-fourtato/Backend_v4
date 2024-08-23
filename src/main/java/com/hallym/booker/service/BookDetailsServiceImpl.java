package com.hallym.booker.service;

import com.hallym.booker.controller.ApiTagValue;
import com.hallym.booker.dto.BookDetails.BookDetailsResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class BookDetailsServiceImpl implements BookDetailsService {

    @Value("${aladin.api.key}")
    private String apiKey;

    private final ApiTagValue apiTagValue;

    // 책 상세정보 조회
    @Override
    public BookDetailsResponseDTO getBookDetailsByISBN(String isbn) {
        try {
            String url = "http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx?ttbkey=" + apiKey + "&itemIdType=ISBN13&ItemId=" + isbn + "&output=js&Version=20131101&Cover=Big";

            Document doc = getDocument(url);
            if (doc != null) {
                Element itemElement = (Element) doc.getElementsByTagName("item").item(0);
                return BookDetailsResponseDTO.builder()
                        .isbn(apiTagValue.getTagValue("isbn", itemElement))
                        .bookTitle(apiTagValue.getTagValue("title", itemElement))
                        .author(apiTagValue.getTagValue("author", itemElement))
                        .publisher(apiTagValue.getTagValue("publisher", itemElement))
                        .coverImageUrl(apiTagValue.getTagValue("cover", itemElement))
                        .build();
            }
        } catch (Exception e) {
            // Handle exception appropriately
            e.printStackTrace();
        }
        return null;
    }

    private Document getDocument(String url) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        return dBuilder.parse(url);
    }
}
