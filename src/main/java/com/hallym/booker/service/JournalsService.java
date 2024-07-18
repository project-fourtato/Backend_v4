package com.hallym.booker.service;

import com.hallym.booker.dto.Journals.JournalsBookInfoResponseDTO;
import com.hallym.booker.dto.Journals.JournalsDeleteDTO;
import com.hallym.booker.dto.Journals.JournalsListResponseDTO;
import com.hallym.booker.dto.Journals.JournalsResponseDTO;

import java.util.List;

public interface JournalsService {

    // 독서록 조회
    JournalsResponseDTO getJournalById(Long journalId);

    // 한 책에 대한 독서록 목록 조회
    List<JournalsListResponseDTO> getJournalsList(Long bookUid);

    // 해당 독서록의 책 상세정보 조회
    JournalsBookInfoResponseDTO getBookDetailsByISBN(String isbn);

    // 독서록 삭제
    String deleteJournal(JournalsDeleteDTO journalsDeleteDTO);
}