import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { NgxSpinnerService } from 'ngx-spinner';
import { LogModel } from 'src/app/model/logs.model';
import { LogService } from 'src/app/services/logs/logs.service';
import { RulesService } from 'src/app/services/rules/rules.service';

@Component({
  selector: 'app-logs',
  templateUrl: './logs.component.html',
  styleUrls: ['./logs.component.css']
})
export class LogsComponent implements OnInit {

  levels: String[] = [ 'INFO',
  'WARN', 'ERROR', 'TRACE', 'DEBUG']

  displayedColumns: string[] = ['level', 'logTime', 'logSource', 'ip', 'alarmDescription', 'alarm'];
  dataSource: MatTableDataSource<LogModel>;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  alarms: boolean = false;
  filterForm: FormGroup;
  alarmForm: FormGroup;


  constructor(private logService: LogService, private spinnerService: NgxSpinnerService, private fb: FormBuilder, private ruleService: RulesService, private _snackBar: MatSnackBar) {
      this.filterForm = fb.group({
        'logType': [""],
        'logSource': [""],
        'dateFrom': [],
        'dateTo': []
      });

      this.alarmForm = fb.group({
        'ruleName': [""],
        'levelInput': [""],
        'messageInput': [""]
      });

   }

  ngOnInit(): void {
    this.getAllLogs();

  }

  getAllLogs(){
    this.spinnerService.show();
    this.logService.getAllLogs().toPromise().then( res => {
      console.log(res);
      this.dataSource = new MatTableDataSource<LogModel>(res);
      this.dataSource.paginator = this.paginator;
      this.spinnerService.hide();
    }, err => {
      this.spinnerService.hide();
    })
  }

  getAlarmLogs(){
    this.spinnerService.show();
    this.logService.getAlarmLogs().toPromise().then( res => {
      console.log(res);
      this.dataSource = new MatTableDataSource<LogModel>(res);
      this.dataSource.paginator = this.paginator;
      this.spinnerService.hide();
    }, err => {
      this.spinnerService.hide();
    })
  }

  showMessage(row){
    confirm(row.message);
  }

  onCheckboxChange(){
    this.alarms = !this.alarms;
    this.filterLogs();
  }

  filterLogs(){
    if(this.filterForm.controls['logType']?.value === "" && this.filterForm.controls['logSource']?.value === "" && this.filterForm.controls['dateFrom']?.value === null
    && this.filterForm.controls['dateTo']?.value === null) {
      this.alarms ? this.getAlarmLogs() : this.getAllLogs();
      return;
    }
    this.spinnerService.show();
    this.logService.filterLogs(this.filterForm.value).toPromise().then( res => {
      if(this.alarms){
        res = res.filter( element => element.alarm)
      }
      this.dataSource = new MatTableDataSource<LogModel>(res);
      this.dataSource.paginator = this.paginator;
      this.spinnerService.hide();
    }, err => {
      this.spinnerService.hide();
      this._snackBar.open("Something went wrong: " + err.message, "Close");

    })
  }

  updateDate(event){
    console.log(event);
  }


  addRule(){
    this.ruleService.addAdminRule(this.alarmForm.value).toPromise().then( res => {
      this._snackBar.open("Rule successfully added.", "Close");
      this.alarmForm.reset();
    }, err => {
      this._snackBar.open("Something went wrong: " + err.message, "Close");
    })
  }

  typeChanged(value, controlName){
    this.alarmForm.controls[controlName].patchValue(value);
    console.log(this.alarmForm.controls[controlName].value)
  }

}
