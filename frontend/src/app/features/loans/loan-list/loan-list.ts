import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { LoanService } from '../../../core/service/loan.service';
import { AuthService } from '../../../core/service/auth.service';

@Component({
  selector: 'app-loan-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './loan-list.html',
  styleUrls: ['./loan-list.css']
})
export class LoanListComponent implements OnInit {
  private loanService = inject(LoanService);
  private authService = inject(AuthService);
  private router = inject(Router);

  loans = signal<any[]>([]);

  totalLoansAmount = computed(() => {
    return this.loans().reduce((acc, loan) => acc + (loan.amount || 0), 0);
  });

  ngOnInit(): void {
    this.loadLoans();
  }

  loadLoans(): void {
    this.loanService.getLoans().subscribe({
      next: (data) => this.loans.set(data),
      error: (err) => console.error(err)
    });
  }

  logout(): void {
    this.authService.logout();
  }
}
