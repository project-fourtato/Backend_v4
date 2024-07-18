package com.hallym.booker.service;

import com.hallym.booker.domain.Journals;
import com.hallym.booker.domain.UserBooks;
import com.hallym.booker.dto.journals.JournalSaveRequest;
import com.hallym.booker.exception.journals.NoJournalContentException;
import com.hallym.booker.exception.journals.NoSuchUserBooksException;
import com.hallym.booker.repository.JournalsRepository;
import com.hallym.booker.repository.UserBooksRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JournalsService {
    private final JournalsRepository journalsRepository;
    private final UserBooksRepository userBooksRepository;

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
}
