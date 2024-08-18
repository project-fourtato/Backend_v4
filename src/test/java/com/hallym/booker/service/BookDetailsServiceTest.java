package com.hallym.booker.service;

import com.hallym.booker.controller.ApiTagValue;
import com.hallym.booker.dto.BookDetails.BookDetailsResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class BookDetailsServiceTest {

    /*@Mock
    private ApiTagValue apiTagValue;

    @InjectMocks
    private BookDetailsService bookDetailsService;

    @Value("${aladin.api.key}")
    private String apiKey;

    @Mock
    private Element itemElement;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getBookDetailsByISBN() throws ParserConfigurationException, IOException, SAXException {
        // Given
        String isbn = "9781234567890";
        String title = "title";
        String author = "author";
        String publisher = "publisher";
        String coverImageUrl = "cover.jpg";

        when(apiTagValue.getTagValue("title", itemElement)).thenReturn(title);
        when(apiTagValue.getTagValue("author", itemElement)).thenReturn(author);
        when(apiTagValue.getTagValue("publisher", itemElement)).thenReturn(publisher);
        when(apiTagValue.getTagValue("cover", itemElement)).thenReturn(coverImageUrl);

        // When
        BookDetailsResponseDTO response = bookDetailsService.getBookDetailsByISBN(isbn);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getIsbn()).isEqualTo(isbn);
        assertThat(response.getBookTitle()).isEqualTo(title);
        assertThat(response.getAuthor()).isEqualTo(author);
        assertThat(response.getPublisher()).isEqualTo(publisher);
        assertThat(response.getCoverImageUrl()).isEqualTo(coverImageUrl);
    }*/
}
