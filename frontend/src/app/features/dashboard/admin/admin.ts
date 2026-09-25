import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoanService } from '../../../core/service/loan.service';
import { AuthService } from '../../../core/service/auth.service';
import { Subject, debounceTime, distinctUntilChanged, switchMap, of } from 'rxjs';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin.html',
  styleUrls: ['./admin.css']
})
export class AdminComponent implements OnInit {
  private loanService = inject(LoanService);
  private authService = inject(AuthService);

  private searchSubject = new Subject<string>();
  allLoans: any[] = [];
  loans = signal<any[]>([]);

  ngOnInit(): void {
    this.loadAllLoans();

    this.searchSubject.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(term => {
        if (!term.trim()) return of(this.allLoans);
        const filtered = this.allLoans.filter(loan =>
          loan.amount.toString().includes(term) ||
          loan.userEmail.toLowerCase().includes(term.toLowerCase())
        );
        return of(filtered);
      })
    ).subscribe(filteredLoans => {
      this.loans.set(filteredLoans);
    });
  }

  loadAllLoans(): void {
    this.loanService.getLoans().subscribe({
      next: (data) => {
        this.allLoans = data;
        this.loans.set(data);
      },
      error: (err) => console.error(err)
    });
  }

  onSearch(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.searchSubject.next(input.value);
  }

  changeStatus(id: number, status: string): void {
    this.loanService.updateLoanStatus(id, status).subscribe({
      next: () => this.loadAllLoans(),
      error: (err) => console.error(err)
    });
  }

  logout(): void {
    this.authService.logout();
  }
}
