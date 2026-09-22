package com.example.backend.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanResponseDTO {

    private Long id;
    private Double amount;
    private Integer term;
    private String status;
    private String userEmail;
    private LocalDateTime createdAt;
}
