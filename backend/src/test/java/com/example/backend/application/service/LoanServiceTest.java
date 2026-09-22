package com.example.backend.application.service;

import com.example.backend.application.dto.LoanRequestDTO;
import com.example.backend.application.dto.LoanResponseDTO;
import com.example.backend.domain.exception.InvalidLoanStateException;
import com.example.backend.domain.exception.LoanNotFoundException;
import com.example.backend.domain.model.Loan;
import com.example.backend.domain.model.LoanStatus;
import com.example.backend.domain.model.Role;
import com.example.backend.domain.model.User;
import com.example.backend.infrastructure.mapper.LoanMapper;
import com.example.backend.infrastructure.persistence.LoanRepository;
import com.example.backend.infrastructure.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LoanMapper loanMapper;

    @InjectMocks
    private LoanService loanService;

    private User user;
    private Loan loan;
    private LoanRequestDTO loanRequestDTO;
    private LoanResponseDTO loanResponseDTO;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("usuario@test.com")
                .password("123")
                .role(Role.ROLE_USER)
                .build();

        loan = Loan.builder()
                .id(1L)
                .amount(1000.0)
                .term(12)
                .status(LoanStatus.PENDING)
                .user(user)
                .build();

        loanRequestDTO = new LoanRequestDTO(1000.0, 12);

        loanResponseDTO = LoanResponseDTO.builder()
                .id(1L)
                .amount(1000.0)
                .term(12)
                .status("PENDING")
                .userEmail("usuario@test.com")
                .build();
    }

    @Test
    void createLoan_Success() {
        when(userRepository.findByEmail("usuario@test.com")).thenReturn(Optional.of(user));
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);
        when(loanMapper.toResponseDTO(any(Loan.class))).thenReturn(loanResponseDTO);

        LoanResponseDTO result = loanService.createLoan(loanRequestDTO, "usuario@test.com");

        assertNotNull(result);
        assertEquals("PENDING", result.getStatus());
        assertEquals(1000.0, result.getAmount());
        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    void createLoan_ThrowsUsernameNotFoundException() {
        when(userRepository.findByEmail("desconocido@test.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            loanService.createLoan(loanRequestDTO, "desconocido@test.com");
        });

        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    void getLoansByUser_Success() {
        when(userRepository.findByEmail("usuario@test.com")).thenReturn(Optional.of(user));
        when(loanRepository.findByUser(user)).thenReturn(Collections.singletonList(loan));
        when(loanMapper.toResponseDTO(loan)).thenReturn(loanResponseDTO);

        List<LoanResponseDTO> result = loanService.getLoansByUser("usuario@test.com");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("PENDING", result.get(0).getStatus());
    }

    @Test
    void getAllLoans_Success() {
        when(loanRepository.findAll()).thenReturn(Collections.singletonList(loan));
        when(loanMapper.toResponseDTO(loan)).thenReturn(loanResponseDTO);

        List<LoanResponseDTO> result = loanService.getAllLoans();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void updateLoanStatus_Success() {
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        loan.setStatus(LoanStatus.APPROVED);
        loanResponseDTO.setStatus("APPROVED");
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);
        when(loanMapper.toResponseDTO(any(Loan.class))).thenReturn(loanResponseDTO);

        LoanResponseDTO result = loanService.updateLoanStatus(1L, LoanStatus.APPROVED, "admin@test.com");

        assertNotNull(result);
        assertEquals("APPROVED", result.getStatus());
        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    void updateLoanStatus_ThrowsInvalidLoanStateException_WhenNotPending() {
        loan.setStatus(LoanStatus.APPROVED);
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        assertThrows(InvalidLoanStateException.class, () -> {
            loanService.updateLoanStatus(1L, LoanStatus.REJECTED, "admin@test.com");
        });

        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    void updateLoanStatus_ThrowsLoanNotFoundException() {
        when(loanRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(LoanNotFoundException.class, () -> {
            loanService.updateLoanStatus(1L, LoanStatus.APPROVED, "admin@test.com");
        });

        verify(loanRepository, never()).save(any(Loan.class));
    }
}
