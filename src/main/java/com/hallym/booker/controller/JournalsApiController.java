package com.hallym.booker.controller;

import com.hallym.booker.dto.Journals.*;
import com.hallym.booker.exception.journals.NoJournalContentException;
import com.hallym.booker.exception.journals.NoSuchUserBooksException;
import com.hallym.booker.global.S3.S3Service;
import com.hallym.booker.global.S3.dto.S3ResponseUploadEntity;
import com.hallym.booker.service.JournalsService;
import com.hallym.booker.service.JournalsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class JournalsApiController {
    private final JournalsService journalsService;
    private final S3Service s3Service;

    @PostMapping("/journals/new/{bookUid}")
    public ResponseEntity<String> journalsSave(@PathVariable Long bookUid,
                                               @RequestParam(required = false) MultipartFile file,
                                               @RequestParam(required = false) String jtitle,
                                               @RequestParam(required = false) String jcontents) throws IOException {
        String imageName = "";
        String imageUrl = "";
        if(file == null) {
            imageName = "default-profile.png";
            imageUrl = "https://booker-v4-bucket.s3.amazonaws.com/default/default-profile.png";
        } else {
            S3ResponseUploadEntity s3Image = s3Service.upload(file, "journal");
            imageName = s3Image.getImageName();
            imageUrl = s3Image.getImageUrl();
        }

        if(jtitle == null || jcontents == null) {
            throw new NoJournalContentException();
        } else {
            JournalSaveRequest journalSaveRequest = new JournalSaveRequest(bookUid, jtitle, jcontents, imageUrl, imageName);
            journalsService.journalSave(journalSaveRequest);
        }
        return new ResponseEntity<>("Save Journals Success", HttpStatus.OK);
    }

    @GetMapping("/journals/{journalId}/edit")
    public JournalsEditFormResponse getJournalsEditForm(@PathVariable Long journalId) {
        return journalsService.getJournalsEditForm(journalId);
    }

    @PutMapping("/journals/{journalId}/edit")
    public ResponseEntity<String> journalsEdit(@PathVariable Long journalId,
                                               @RequestParam(required = false) MultipartFile file,
                                               @RequestParam(required = false) String jtitle,
                                               @RequestParam(required = false) String jcontents) throws IOException {
        JournalsEditRequest journalsEditRequest = new JournalsEditRequest(journalId, jtitle, jcontents, file);
        journalsService.journalsEdit(journalsEditRequest);
        return new ResponseEntity<>("Edit Journals Success", HttpStatus.OK);
    }



    // 독서록 조회 API
    @GetMapping("/journals/{journalId}")
    public ResponseEntity<JournalsResponseDTO> getJournal(@PathVariable Long journalId) {
        JournalsResponseDTO journal = journalsService.getJournalById(journalId);
        return ResponseEntity.ok().body(journal);
    }

    // 한 책에 대한 독서록 목록 조회 API
    @GetMapping("/journals/journalsList/{bookUid}")
    public ResponseEntity<Map<String, List<JournalsListResponseDTO>>> getJournalsList(@PathVariable Long bookUid) {
        List<JournalsListResponseDTO> journalsList = journalsService.getJournalsList(bookUid);

        if (journalsList.isEmpty()) {
            throw new NoSuchUserBooksException();
        }

        Map<String, List<JournalsListResponseDTO>> response = new HashMap<>();
        response.put("data", journalsList);

        return ResponseEntity.ok().body(response);
    }

    // 해당 독서록의 책 상세정보 조회 API
    @GetMapping("/journals/bookDetailsByISBN/{isbn}")
    public ResponseEntity<JournalsBookInfoResponseDTO> getBookDetailsByISBN(@PathVariable String isbn) {
        JournalsBookInfoResponseDTO bookDetails = journalsService.getBookDetailsByISBN(isbn);
        return ResponseEntity.ok().body(bookDetails);
    }

    // 독서록 삭제 API
    @PostMapping("/journals/{journalId}/delete")
    public ResponseEntity<String> deleteJournal(@PathVariable Long journalId) {
        JournalsDeleteDTO deleteDTO = JournalsDeleteDTO.builder().journalId(journalId).build();
        String result = journalsService.deleteJournal(deleteDTO);
        return ResponseEntity.ok().body(result);
    }
}
