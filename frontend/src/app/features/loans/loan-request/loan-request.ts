import { Component, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { LoanService } from '../../../core/service/loan.service';

@Component({
  selector: 'app-loan-request',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './loan-request.html',
  styleUrls: ['./loan-request.css']
})
export class LoanRequestComponent {
  private fb = inject(FormBuilder);
  private loanService = inject(LoanService);
  private router = inject(Router);

  errorMessage: string | null = null;
  amountSignal = signal<number>(0);

  estimatedInterest = computed(() => {
    return this.amountSignal() * 0.05;
  });

  totalPayable = computed(() => {
    return this.amountSignal() + this.estimatedInterest();
  });

  loanForm = this.fb.group({
    amount: [null, [Validators.required, Validators.min(1)]],
    term: [null, [Validators.required, Validators.min(1)]]
  });

  constructor() {
    this.loanForm.get('amount')?.valueChanges.subscribe(val => {
      this.amountSignal.set(Number(val || 0));
    });
  }

  onSubmit(): void {
    if (this.loanForm.invalid) {
      return;
    }

    this.errorMessage = null;
    const rawValue = this.loanForm.value;
    const payload = {
      amount: Number(rawValue.amount),
      term: Number(rawValue.term)
    };

    this.loanService.createLoan(payload).subscribe({
      next: () => {
        this.router.navigate(['/loans/list']);
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Error al solicitar el prestamo';
      }
    });
  }
}
