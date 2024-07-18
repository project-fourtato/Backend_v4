package com.hallym.booker.controller;

import com.hallym.booker.dto.Journals.JournalsBookInfoResponseDTO;
import com.hallym.booker.dto.Journals.JournalsDeleteDTO;
import com.hallym.booker.dto.Journals.JournalsListResponseDTO;
import com.hallym.booker.dto.Journals.JournalsResponseDTO;
import com.hallym.booker.exception.userBooks.NoSuchUserBooksException;
import com.hallym.booker.service.JournalsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Journals")
public class JournalsController {

    private final JournalsService journalsService;

    // 독서록 조회 API
    @GetMapping("/{journalId}")
    public ResponseEntity<JournalsResponseDTO> getJournal(@PathVariable Long journalId) {
        JournalsResponseDTO journal = journalsService.getJournalById(journalId);
        return ResponseEntity.ok().body(journal);
    }

    // 한 책에 대한 독서록 목록 조회 API
    @GetMapping("/journalsList/{bookUid}")
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
    @GetMapping("/bookDetailsByISBN/{isbn}")
    public ResponseEntity<JournalsBookInfoResponseDTO> getBookDetailsByISBN(@PathVariable String isbn) {
        JournalsBookInfoResponseDTO bookDetails = journalsService.getBookDetailsByISBN(isbn);
        return ResponseEntity.ok().body(bookDetails);
    }

    // 독서록 삭제 API
    @PostMapping("/{journalId}/delete")
    public ResponseEntity<String> deleteJournal(@PathVariable Long journalId) {
        JournalsDeleteDTO deleteDTO = JournalsDeleteDTO.builder().journalId(journalId).build();
        String result = journalsService.deleteJournal(deleteDTO);
        return ResponseEntity.ok().body(result);
    }

}
