package com.hallym.booker.controller;

import com.amazonaws.Response;
import com.hallym.booker.dto.journals.JournalSaveRequest;
import com.hallym.booker.dto.journals.JournalsEditFormResponse;
import com.hallym.booker.dto.journals.JournalsEditRequest;
import com.hallym.booker.exception.journals.NoJournalContentException;
import com.hallym.booker.global.S3.S3Service;
import com.hallym.booker.global.S3.dto.S3ResponseUploadEntity;
import com.hallym.booker.service.JournalsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
}
