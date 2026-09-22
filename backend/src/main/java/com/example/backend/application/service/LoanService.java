package com.example.backend.application.service;

import com.example.backend.application.dto.LoanRequestDTO;
import com.example.backend.application.dto.LoanResponseDTO;
import com.example.backend.domain.exception.InvalidLoanStateException;
import com.example.backend.domain.exception.LoanNotFoundException;
import com.example.backend.domain.exception.UnauthorizedLoanAccessException;
import com.example.backend.domain.model.Loan;
import com.example.backend.domain.model.LoanStatus;
import com.example.backend.domain.model.Role;
import com.example.backend.domain.model.User;
import com.example.backend.infrastructure.mapper.LoanMapper;
import com.example.backend.infrastructure.persistence.LoanRepository;
import com.example.backend.infrastructure.persistence.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final LoanMapper loanMapper;

    public LoanService(LoanRepository loanRepository, UserRepository userRepository, LoanMapper loanMapper) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.loanMapper = loanMapper;
    }

    @Transactional
    @CacheEvict(value = "loansCache", allEntries = true)
    public LoanResponseDTO createLoan(LoanRequestDTO dto, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Loan loan = Loan.builder()
                .amount(dto.getAmount())
                .term(dto.getTerm())
                .status(LoanStatus.PENDING)
                .user(user)
                .build();

        Loan savedLoan = loanRepository.save(loan);
        return loanMapper.toResponseDTO(savedLoan);
    }

    @Cacheable(value = "loansCache", key = "#userEmail")
    @Transactional(readOnly = true)
    public List<LoanResponseDTO> getLoansByUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return loanRepository.findByUser(user).stream()
                .map(loanMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "loansCache", key = "'all_loans'")
    @Transactional(readOnly = true)
    public List<LoanResponseDTO> getAllLoans() {
        return loanRepository.findAll().stream()
                .map(loanMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(value = "loansCache", allEntries = true)
    public LoanResponseDTO updateLoanStatus(Long loanId, LoanStatus newStatus, String adminEmail) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Prestamo no encontrado con ID: " + loanId));

        if (loan.getStatus() != LoanStatus.PENDING) {
            throw new InvalidLoanStateException("Solo se pueden modificar prestamos en estado PENDING");
        }

        loan.setStatus(newStatus);
        Loan updatedLoan = loanRepository.save(loan);
        return loanMapper.toResponseDTO(updatedLoan);
    }
}
