package com.hallym.booker.service;

import com.hallym.booker.domain.Journals;
import com.hallym.booker.domain.UserBooks;
import com.hallym.booker.dto.journals.JournalSaveRequest;
import com.hallym.booker.dto.journals.JournalsEditFormResponse;
import com.hallym.booker.dto.journals.JournalsEditRequest;
import com.hallym.booker.exception.journals.NoJournalContentException;
import com.hallym.booker.exception.journals.NoSuchJournalsException;
import com.hallym.booker.exception.journals.NoSuchUserBooksException;
import com.hallym.booker.global.S3.S3Service;
import com.hallym.booker.global.S3.dto.S3ResponseUploadEntity;
import com.hallym.booker.repository.JournalsRepository;
import com.hallym.booker.repository.UserBooksRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JournalsService {
    private final JournalsRepository journalsRepository;
    private final UserBooksRepository userBooksRepository;
    private final S3Service s3Service;

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
