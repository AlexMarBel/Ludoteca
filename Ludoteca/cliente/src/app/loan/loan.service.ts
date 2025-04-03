import { Injectable } from '@angular/core';
import { Pageable } from '../../core/model/page/Pageable';
import { Observable, of } from 'rxjs';
import { LoanPage } from './model/LoanPage';
import { LOAN_DATA } from './model/mock-loans';
import { Loan } from './model/Loan';
import { HttpClient } from '@angular/common/http';
import { DatePipe, formatDate } from '@angular/common';

@Injectable({
  providedIn: 'root',
})
export class LoanService {

  constructor(private http: HttpClient) { }

  private baseUrl = 'http://localhost:8080/loan';

  getLoans(pageable: Pageable, gameId?: number, clientId?: number, date?: Date): Observable<LoanPage>{
    const params = new URLSearchParams();
    if (gameId) {
      params.set('gameId', gameId.toString());
    }
    if (clientId) {
      params.set('clientId', clientId.toString());
    }
    if(date){
      const formatedDate = new DatePipe('en-US').transform(date, 'yyyy-MM-dd');
      params.set('date', formatedDate);
    }

    const queryString = params.toString();
    const url = `${this.baseUrl}?${queryString}`;

    return this.http.post<LoanPage>(url, {pageable: pageable});
  }

  saveLoan(loan: Loan): Observable<Loan>{
    
    const url = this.baseUrl;
    return this.http.put<Loan>(url, loan);
  }

  deleteLoan(idLoan: number): Observable<void>{
    return this.http.delete<void>(`${this.baseUrl}/${idLoan}`)
  }
  
  getAllLoans(): Observable<Loan[]>{
    return this.http.get<Loan[]>(this.baseUrl);
  }
}
