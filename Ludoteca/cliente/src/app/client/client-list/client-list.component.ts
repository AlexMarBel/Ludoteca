import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { Client } from '../model/Client';
import { ClientService } from '../client.service';
import { MatDialog } from '@angular/material/dialog';
import { ClientEditComponent } from '../client-edit/client-edit.component';
import { DialogConfirmationComponent } from '../../../core/dialog-confirmation/dialog-confirmation.component';
import { D } from '@angular/cdk/keycodes';

@Component({
  selector: 'app-client-list',
  imports: [
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    CommonModule
  ],
  templateUrl: './client-list.component.html',
  styleUrl: './client-list.component.scss'
})
export class ClientListComponent {
  dataSource = new MatTableDataSource<Client>();
  displayedColumns: string[] = ['id', 'name', 'action'];

  constructor(
    private clientService: ClientService,
    public dialog: MatDialog,
  ) { }

  ngOnInit(): void {
    this.clientService.getClients().subscribe((clients) => (this.dataSource.data = clients));
  }

  createClient(){
    const dialogRef = this.dialog.open(ClientEditComponent, {
      data: { clients: this.dataSource.data, client: null}
    });

    dialogRef.afterClosed().subscribe(result => {
      this.ngOnInit();
    });

  }

  editClient(client: Client){
    const dialogRef = this.dialog.open(ClientEditComponent, {
      data: { clients: this.dataSource.data, client: client }
    });


    dialogRef.afterClosed().subscribe(result => {
      this.ngOnInit();
    });
  }

  deleteClient(client: Client){
    const dialogRef = this.dialog.open(DialogConfirmationComponent,{
      data: {title: "Eliminar cliente",
        description: "Atención si borra el cliente se perderán sus datos"}
      });

      dialogRef.afterClosed().subscribe(result => {
        if(result){
          this.clientService.deleteClient(client.id).subscribe(() => {
            this.ngOnInit();
          }); 
        }
      });
    }
}
