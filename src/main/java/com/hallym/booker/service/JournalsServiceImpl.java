package com.hallym.booker.service;

import com.hallym.booker.controller.ApiTagValue;
import com.hallym.booker.domain.Journals;
import com.hallym.booker.domain.UserBooks;
import com.hallym.booker.dto.Journals.*;
import com.hallym.booker.exception.journals.NoSuchBookUidException;
import com.hallym.booker.exception.journals.NoSuchJournalsException;
import com.hallym.booker.exception.journals.NoSuchUserBooksException;
import com.hallym.booker.global.S3.S3Service;
import com.hallym.booker.global.S3.dto.S3ResponseUploadEntity;
import com.hallym.booker.repository.JournalsRepository;
import com.hallym.booker.repository.UserBooksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JournalsServiceImpl implements JournalsService {

    @Autowired
    private final JournalsRepository journalsRepository;
    private final UserBooksRepository userBooksRepository;
    private final S3Service s3Service;

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

    /**
     * 독서록 등록
     */
    @Transactional
    public void journalSave(JournalSaveRequest journalSaveRequest) {
        UserBooks userBooks = userBooksRepository.findById(journalSaveRequest.getBookUid()).orElseThrow(NoSuchUserBooksException::new);

        Journals journals = Journals.create(userBooks, journalSaveRequest.getJtitle(), journalSaveRequest.getJcontents(),
                LocalDateTime.now(), journalSaveRequest.getJimageUrl(), journalSaveRequest.getJimageName());

        journalsRepository.save(journals);
    }

    /**
     * 독서록 수정 폼
     */
    public JournalsEditFormResponse getJournalsEditForm(Long journalId) {
        Journals journals = journalsRepository.findById(journalId).orElseThrow(NoSuchJournalsException::new);

        return new JournalsEditFormResponse(journalId,
                journals.getJdatetime(), journals.getJtitle(), journals.getJcontents(),
                journals.getJimageUrl(), journals.getJimageName());
    }

    /**
     * 독서록 수정
     */
    @Transactional
    public void journalsEdit(JournalsEditRequest journalsEditRequest) throws IOException {
        Journals journals = journalsRepository.findById(journalsEditRequest.getJournalId()).orElseThrow(NoSuchJournalsException::new);

        String imageName = journals.getJimageName();
        String imageUrl = journals.getJimageUrl();
        if(journalsEditRequest.getFile() != null) {
            if(!journals.getJimageName().equals("default-profile.png")) {
                s3Service.delete(journals.getJimageName());
            }
            S3ResponseUploadEntity journal = s3Service.upload(journalsEditRequest.getFile(), "journal");

            imageName = journal.getImageName();
            imageUrl = journal.getImageUrl();
        }

        journals.change(journalsEditRequest.getJtitle(), LocalDateTime.now(), journalsEditRequest.getJcontents(), imageUrl, imageName);
    }

}
