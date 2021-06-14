import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { Message } from 'src/app/model/message.model';
import { PatientModel } from 'src/app/model/patient.model';
import { MessagesService } from 'src/app/services/messages/messages.service';

@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.css']
})
export class MessagesComponent implements OnInit {

  displayedColumns: string[] = ['patient', 'dateTime', 'type', 'alarm'];
  dataSource: MatTableDataSource<Message>;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  alarms: boolean = false;
  formData: FormGroup;


  constructor(private messageService: MessagesService, private fb: FormBuilder) {
    this.formData = fb.group({
      'name': null,
      'surname': null
    })
    this.getAllMessages();
    
  }

  ngOnInit(): void {
  }

  onCheckboxChange(){
    this.alarms = !this.alarms;
    this.alarms ? this.getAllAlarms() : this.getAllMessages();
  }

  getAllMessages(){
    this.messageService.getMessages().toPromise().then( res => {
      this.dataSource = new MatTableDataSource<Message>(res);
      this.dataSource.paginator = this.paginator;
      console.log(this.dataSource.data)
    })
  }

  getAllAlarms(){
    this.messageService.getAlarms().toPromise().then( res => {
      this.dataSource = new MatTableDataSource<Message>(res);
      this.dataSource.paginator = this.paginator;
    })
  }

  showMessage(row){
    confirm(row.message);
  }

}
