package com.example.backend.infrastructure.persistence;

import com.example.backend.domain.model.Loan;
import com.example.backend.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUser(User user);
}
