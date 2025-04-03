import { Component, Inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Client } from '../model/Client';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ClientService } from '../client.service';

@Component({
  selector: 'app-client-edit',
  imports: [
    FormsModule, 
    ReactiveFormsModule, 
    MatFormFieldModule, 
    MatInputModule, 
    MatButtonModule],
  templateUrl: './client-edit.component.html',
  styleUrl: './client-edit.component.scss'
})
export class ClientEditComponent implements OnInit{
  client: Client;
  clients: Client[];
  
  constructor(
    public dialogRef: MatDialogRef<ClientEditComponent>,
    private clientService: ClientService,
    @Inject(MAT_DIALOG_DATA) public data: { clients: Client[], client: Client },
  ) {}

  ngOnInit(): void {
    this.client = this.data.client ? Object.assign({}, this.data.client) : new Client();
    this.clients = this.data.clients;
  }

  onSave(){
    
    const nameExists = this.clients.some(c => c.name.toLowerCase() === this.client.name.toLowerCase());
    
    if (nameExists) {
      alert('Ya existe un cliente con este nombre');
      return;
    }
       
    
    this.clientService.saveClient(this.client).subscribe(() =>{
      this.dialogRef.close();
    });
  }

  onClose(){
    this.dialogRef.close();
  }
}
