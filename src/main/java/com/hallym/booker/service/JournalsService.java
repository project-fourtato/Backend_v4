package com.hallym.booker.service;


import com.hallym.booker.dto.Journals.*;

import java.io.IOException;
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

    //독서록 등록
    void journalSave(JournalSaveRequest journalSaveRequest);

    //독서록 수정 폼
    JournalsEditFormResponse getJournalsEditForm(Long journalId);

    //독서록 수정
    void journalsEdit(JournalsEditRequest journalsEditRequest) throws IOException;
}

