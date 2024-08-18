package com.hallym.booker.service;

import com.hallym.booker.domain.Interests;
import com.hallym.booker.domain.Login;
import com.hallym.booker.dto.Interests.InterestsResponseDTO;
import com.hallym.booker.repository.InterestsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InterestsServiceTest {

    @Mock
    private InterestsRepository interestsRepository;

    @InjectMocks
    private InterestsService interestsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getInterestsByProfile() {
        // Given
        Login login = mock(Login.class);
        Long profileUid = login.getProfile().getProfileUid();
        Interests interest = mock(Interests.class);
        when(interestsRepository.findByProfile_ProfileUid(profileUid)).thenReturn(List.of(interest));

        // When
        List<String> result = interestsService.getInterestsByProfile(login.getLoginUid());

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(1);
    }

}
