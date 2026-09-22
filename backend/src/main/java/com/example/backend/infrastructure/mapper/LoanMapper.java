package com.example.backend.infrastructure.mapper;

import com.example.backend.application.dto.LoanResponseDTO;
import com.example.backend.domain.model.Loan;
import org.springframework.stereotype.Component;

@Component
public class LoanMapper {

    public LoanResponseDTO toResponseDTO(Loan loan) {
        if (loan == null) {
            return null;
        }
        return LoanResponseDTO.builder()
                .id(loan.getId())
                .amount(loan.getAmount())
                .term(loan.getTerm())
                .status(loan.getStatus().name())
                .userEmail(loan.getUser().getEmail())
                .createdAt(loan.getCreatedAt())
                .build();
    }
}
