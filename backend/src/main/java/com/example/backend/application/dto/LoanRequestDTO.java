package com.example.backend.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequestDTO {

    @NotNull
    @Positive
    private Double amount;

    @NotNull
    @Positive
    private Integer term;
}
