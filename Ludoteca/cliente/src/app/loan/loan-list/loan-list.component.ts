import { CommonModule, DatePipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { Loan } from '../model/Loan';
import { LoanService } from '../loan.service';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { Pageable } from '../../../core/model/page/Pageable';
import { Client } from '../../client/model/Client';
import { MatFormFieldControl, MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule, provideNativeDateAdapter } from '@angular/material/core';
import { LoanNewComponent } from '../loan-new/loan-new.component';
import { Game } from '../../game/model/Game';
import { GameService } from '../../game/game.service';
import { ClientService } from '../../client/client.service';
import { MatSelectModule } from '@angular/material/select';
import { DialogConfirmationComponent } from '../../../core/dialog-confirmation/dialog-confirmation.component';

@Component({
  selector: 'app-loan-list',
  imports: [
    MatButtonModule, 
    MatIconModule, 
    MatTableModule, 
    CommonModule,
    MatPaginatorModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatSelectModule,
  ],
  providers: [provideNativeDateAdapter(), MatNativeDateModule, DatePipe],
  templateUrl: './loan-list.component.html',
  styleUrl: './loan-list.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LoanListComponent implements OnInit {
  pageNumber: number = 0;
  pageSize: number = 5;
  totalElements: number = 0;

  games: Game[];
  clients: Client[];

  filterClient: Client;
  filterGame: Game;
  filterDate: Date;



  dataSource = new MatTableDataSource<Loan>();
  displayedColumns: string[] = ['id', 'game', 'client', 'startDate', 'endDate', 'actions'];

  constructor(
    private loanService: LoanService, 
    private gameService: GameService,
    private clientService: ClientService,
    private datePipe: DatePipe,
    public dialog: MatDialog) {}

  ngOnInit(): void {
    this.loadPage();
    this.gameService.getGames().subscribe((games) => (this.games = games));
    this.clientService.getClients().subscribe((clients) =>(this.clients = clients))
  }

  loadPage(event?: PageEvent, gameId?: number, clientId?: number, date?: Date){
    const pageable: Pageable = {
      pageNumber: this.pageNumber,
      pageSize: this.pageSize,
      sort: [
        {
          property: 'id',
          direction: 'ASC',
        },
      ],
    };

    if(event != null){
      pageable.pageSize = event.pageSize;
      pageable.pageNumber = event.pageIndex;
    }

    this.loanService.getLoans(pageable, gameId, clientId, date).subscribe((data)=> {
      this.dataSource.data = data.content;
      this.pageNumber = data.pageable.pageNumber;
      this.pageSize = data.pageable.pageSize;
      this.totalElements = data.totalElements;

    });
  }

  onCleanFilter(): void {
    this.filterGame = null;
    this.filterClient = null;
    this.filterDate = null;
    this.onSearch();
}


onSearch(): void {
  const gameId = this.filterGame != null ? this.filterGame.id : null;
  const clientId = this.filterClient != null ? this.filterClient.id : null;
  const date = this.filterDate != null ? this.filterDate : null;

  this.pageNumber = 0;

  this.loadPage(undefined, gameId, clientId, date);
}

  createLoan() {
    const dialogRef = this.dialog.open(LoanNewComponent, {
      data: {},
    });

    dialogRef.afterClosed().subscribe((result) => {
      this.ngOnInit();
    });
  }

  deleteLoan(loan: Loan) {
    const dialogRef = this.dialog.open(DialogConfirmationComponent, {
              data: {
                  title: 'Eliminar préstamo',
                  description:
                      'Atención si borra el préstamo se perderán sus datos.<br> ¿Desea eliminar el préstamo?',
              },
          });
    
          dialogRef.afterClosed().subscribe((result) => {
              if (result) {
                  this.loanService.deleteLoan(loan.id).subscribe((result) => {
                      this.ngOnInit();
                  });
              }
          });
  }

}
