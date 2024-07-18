package com.hallym.booker.service;

import com.hallym.booker.domain.Directmessage;
import com.hallym.booker.domain.Profile;
import com.hallym.booker.dto.Directmessage.DirectmessageResponseDTO;
import com.hallym.booker.exception.directmessage.NoSuchDirectmessageException;
import com.hallym.booker.repository.DirectmessageRepository;
import com.hallym.booker.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DirectmessageServiceTest {

    @Mock
    private DirectmessageRepository directmessageRepository;

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private DirectmessageServiceImpl directmessageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getDirectmessageList() {
        // Given
        Long profileUid = 1L;
        Profile profile = mock(Profile.class);
        when(profileRepository.findById(profileUid)).thenReturn(Optional.of(profile));

        Directmessage message1 = mock(Directmessage.class);
        Directmessage message2 = mock(Directmessage.class);
        when(directmessageRepository.findAllDirectMessagesByRecipient(profileUid)).thenReturn(List.of(message1));
        when(directmessageRepository.findAllDirectmessagesBySender(profileUid)).thenReturn(List.of(message2));

        // When
        List<DirectmessageResponseDTO> result = directmessageService.getDirectmessageList(profileUid);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void getDirectmessageList_NoMessages() {
        // Given
        Long profileUid = 1L;
        when(profileRepository.findById(profileUid)).thenReturn(Optional.of(mock(Profile.class)));
        when(directmessageRepository.findAllDirectMessagesByRecipient(profileUid)).thenReturn(List.of());
        when(directmessageRepository.findAllDirectmessagesBySender(profileUid)).thenReturn(List.of());

        // When / Then
        assertThatThrownBy(() -> directmessageService.getDirectmessageList(profileUid))
                .isInstanceOf(NoSuchDirectmessageException.class);
    }
}
