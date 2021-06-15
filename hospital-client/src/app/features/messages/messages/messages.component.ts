import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { Message } from 'src/app/model/message.model';
import { PatientModel } from 'src/app/model/patient.model';
import { MessagesService } from 'src/app/services/messages/messages.service';
import { RulesService } from 'src/app/services/rules/rules.service';



@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.css']
})
export class MessagesComponent implements OnInit {

  bloodTypes: String[] = ['A_POSITIVE',
  'A_NEGATIVE',
  'B_POSITIVE',
  'B_NEGATIVE',
  'AB_POSITIVE',
  'AB_NEGATIVE',
  'O_POSITIVE',
  'O_NEGATIVE']

  messageTypes: String[] = [ 'HEARTH_BEAT',
    'PRESSURE',
    'TEMPERATURE']

  operations: String[] = ['==',
  '!=',
  '<=',
  '>=',
  '<',
  '>']

  displayedColumns: string[] = ['patient', 'dateTime', 'type', 'alarm'];
  dataSource: MatTableDataSource<Message>;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  alarms: boolean = false;
  searchForm: FormGroup;
  alarmForm: FormGroup;

  constructor(private messageService: MessagesService, private fb: FormBuilder, private ruleService: RulesService, private _snackBar: MatSnackBar) {
    this.searchForm = fb.group({
      'name': [""],
      'surname': [""]
    });

    this.alarmForm = fb.group({
      'ruleName': [""],
      'patient': [""],
      'bloodType': [null],
      'messageType': [null],
      'value': [""],
      'operation': [""]

    })
    this.getAllMessages();
    
  }

  ngOnInit(): void {
  }

  onCheckboxChange(){
    this.alarms = !this.alarms;
    this.search();
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

  search(){
    if(this.searchForm.controls['name']?.value === "" && this.searchForm.controls['surname']?.value === "") {
      this.alarms ? this.getAllAlarms() : this.getAllMessages();
      return;
    }
   
    if(this.alarms){
      this.messageService.getAlarmsByPatient(this.searchForm.controls['name']?.value || "null", this.searchForm.controls['surname']?.value || "null").toPromise().then( res => {
        this.dataSource = new MatTableDataSource<Message>(res);
        this.dataSource.paginator = this.paginator;
      })
    }
    else{
      this.messageService.getMessagesByPatient(this.searchForm.controls['name']?.value  || "null", this.searchForm.controls['surname']?.value || "null").toPromise().then( res => {
        this.dataSource = new MatTableDataSource<Message>(res);
        this.dataSource.paginator = this.paginator;
      })
    }
  }

  typeChanged(value, controlName){
    this.alarmForm.controls[controlName].patchValue(value);
    console.log(this.alarmForm.controls[controlName].value)
  }

  addRule(){
    this.ruleService.addDoctorRule(this.alarmForm.value).toPromise().then( res => {
      this._snackBar.open("Rule successfully added.", "Close");
      this.alarmForm.reset();
    }, err => {
      this._snackBar.open("Something went wrong: " + err.message, "Close");
    })
  }

}
