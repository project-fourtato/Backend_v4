package com.hallym.booker.service;

import com.hallym.booker.controller.ApiTagValue;
import com.hallym.booker.domain.Journals;
import com.hallym.booker.dto.Journals.JournalsBookInfoResponseDTO;
import com.hallym.booker.dto.Journals.JournalsDeleteDTO;
import com.hallym.booker.dto.Journals.JournalsListResponseDTO;
import com.hallym.booker.dto.Journals.JournalsResponseDTO;
import com.hallym.booker.exception.journals.NoSuchBookUidException;
import com.hallym.booker.exception.journals.NoSuchJournalsException;
import com.hallym.booker.exception.userBooks.NoSuchUserBooksException;
import com.hallym.booker.repository.JournalsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JournalsServiceImpl implements JournalsService {

    private final JournalsRepository journalsRepository;
    private final ApiTagValue apiTagValue;

    @Value("${aladin.api.key}")
    private String apiKey;

    // 독서록 조회
    @Override
    public JournalsResponseDTO getJournalById(Long journalId) {
        Journals journal = journalsRepository.findById(journalId)
                .orElseThrow(NoSuchJournalsException::new);

        return JournalsResponseDTO.builder()
                .journalId(journal.getJournalId())
                .jtitle(journal.getJtitle())
                .jcontents(journal.getJcontents())
                .jdatetime(journal.getJdatetime())
                .jimageUrl(journal.getJimageUrl())
                .jimageName(journal.getJimageName())
                .build();
    }

    // 한 책에 대한 독서록 목록 조회
    @Override
    public List<JournalsListResponseDTO> getJournalsList(Long bookUid) {
        List<Journals> journalsList = journalsRepository.findByUserBooks_BookUid(bookUid);

        if (journalsList.isEmpty()) {
            throw new NoSuchBookUidException();
        }

        return journalsList.stream()
                .map(journal -> JournalsListResponseDTO.builder()
                        .journalId(journal.getJournalId())
                        .jtitle(journal.getJtitle())
                        .jcontents(journal.getJcontents())
                        .jdatetime(journal.getJdatetime())
                        .jimageUrl(journal.getJimageUrl())
                        .jimageName(journal.getJimageName())
                        .build())
                .collect(Collectors.toList());
    }

    // 해당 독서록의 책 상세정보 조회
    @Override
    public JournalsBookInfoResponseDTO getBookDetailsByISBN(String isbn) {
        String url = "https://www.aladin.co.kr/ttb/api/ItemLookUp.aspx?ttbkey=" + apiKey + "&itemIdType=ISBN13&ItemId=" + isbn + "&output=xml&Version=20131101&OptResult=cover=Big";

        Document doc;
        try {
            doc = getDocument(url);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new NoSuchUserBooksException();
        }

        if (doc != null) {
            Element itemElement = (Element) doc.getElementsByTagName("item").item(0);
            return JournalsBookInfoResponseDTO.builder()
                    .bookTitle(apiTagValue.getTagValue("title", itemElement))
                    .author(apiTagValue.getTagValue("author", itemElement))
                    .publisher(apiTagValue.getTagValue("publisher", itemElement))
                    .coverImageUrl(apiTagValue.getTagValue("cover", itemElement))
                    .pubDate(apiTagValue.getTagValue("pubDate", itemElement))
                    .description(apiTagValue.getTagValue("description", itemElement))
                    .categoryName(apiTagValue.getTagValue("categoryName", itemElement))
                    .build();
        }

        throw new NoSuchUserBooksException();
    }

    private Document getDocument(String url) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        return dBuilder.parse(url);
    }

    // 독서록 삭제
    @Override
    @Transactional
    public String deleteJournal(JournalsDeleteDTO journalsDeleteDTO) {
        Journals journal = journalsRepository.findById(journalsDeleteDTO.getJournalId())
                .orElseThrow(NoSuchJournalsException::new);
        journalsRepository.delete(journal);
        return "독서록 삭제 완료";
    }

}
