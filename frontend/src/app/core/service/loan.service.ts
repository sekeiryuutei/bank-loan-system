import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoanService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8081/api/loans';

  getLoans(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  createLoan(loanData: { amount: number; term: number }): Observable<any> {
    return this.http.post<any>(this.apiUrl, loanData);
  }

  updateLoanStatus(id: number, status: string): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}/status?status=${status}`, {});
  }
}
