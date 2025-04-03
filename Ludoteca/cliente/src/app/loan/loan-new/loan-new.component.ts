import { ChangeDetectionStrategy, Component, Inject, OnInit } from '@angular/core';
import { Loan } from '../model/Loan';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDialogRef } from '@angular/material/dialog';
import { GameService } from '../../game/game.service';
import { ClientService } from '../../client/client.service';
import { Game } from '../../game/model/Game';
import { Client } from '../../client/model/Client';
import { LoanService } from '../loan.service';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule, DatePipe } from '@angular/common';
import { MatNativeDateModule, provideNativeDateAdapter } from '@angular/material/core';

@Component({
  selector: 'app-loan-new',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatDatepickerModule,
    MatIconModule,
    CommonModule,
    MatNativeDateModule,
  ],
  templateUrl: './loan-new.component.html',
  styleUrl: './loan-new.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [provideNativeDateAdapter(), MatNativeDateModule, DatePipe],
})
export class LoanNewComponent implements OnInit {
  loan: Loan;
  loans: Loan[];
  games: Game[];
  clients: Client[];
  dateStart: Date;
  dateEnd: Date;

  constructor(
    public dialogRef: MatDialogRef<LoanNewComponent>,
    private gameService: GameService,
    private clientService: ClientService,
    private loanService: LoanService,
    private datePipe: DatePipe,
  ) { }

  ngOnInit(): void {
    this.loan = new Loan();

    this.gameService.getGames().subscribe((games) => {
      this.games = games;
    });

    this.clientService.getClients().subscribe((clients) => {
      this.clients = clients;
    });

    this.loanService.getAllLoans().subscribe((loans) => {
      this.loans = loans;
    });
  }


  onSave(): void {

    if (!this.validateDates(this.loan)) {
      alert('La fecha de devolución debe ser como máximo 14 días después de la fecha de inicio y no puede ser anterior a la de inicio.');
      return;
    }

    this.loan.startDate = new Date(this.datePipe.transform(this.loan.startDate, 'yyyy-MM-dd')!);
    this.loan.endDate = new Date(this.datePipe.transform(this.loan.endDate, 'yyyy-MM-dd')!);

    this.loanService.saveLoan(this.loan).subscribe({
      next: (result) => {
        this.dialogRef.close();
      },
      error: (error) => {
        if (error.status === 409) { 
          alert('El juego ya está prestado en esas fechas.');
        }
        if (error.status === 400) { 
          alert('El cliente ya tiene 2 préstamos en esas fechas.');       
        } 
      },
    });
  }

  onClose() {
    this.dialogRef.close();
  }

  validateDates(loan: Loan): boolean {
    if (!loan.startDate || !loan.endDate) {
      return false;
    }
    
    const startDate = new Date(loan.startDate).getTime();
    const endDate = new Date(loan.endDate).getTime();
    const diffTime = (endDate - startDate) 
    const diffDays = diffTime / (1000 * 60 * 60 * 24);

    return diffDays >= 0 && diffDays <= 14;
  }
}
