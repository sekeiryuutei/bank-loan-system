package com.example.backend.infrastructure.web;

import com.example.backend.application.dto.LoanRequestDTO;
import com.example.backend.application.dto.LoanResponseDTO;
import com.example.backend.application.service.LoanService;
import com.example.backend.domain.model.LoanStatus;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
@CrossOrigin(origins = "*")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public LoanResponseDTO createLoan(@Valid @RequestBody LoanRequestDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return loanService.createLoan(dto, auth.getName());
    }

    @GetMapping
    public List<LoanResponseDTO> getLoans() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("SCOPE_ROLE_ADMIN"));

        if (isAdmin) {
            return loanService.getAllLoans();
        }
        return loanService.getLoansByUser(auth.getName());
    }

    @PutMapping("/{id}/status")
    public LoanResponseDTO updateStatus(@PathVariable Long id, @RequestParam LoanStatus status) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return loanService.updateLoanStatus(id, status, auth.getName());
    }
}
