import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { LogsService } from 'src/app/service/logs/logs.service';

@Component({
  selector: 'app-logs',
  templateUrl: './logs.component.html',
  styleUrls: ['./logs.component.css']
})
export class LogsComponent implements OnInit {

  paths: any = [{value: 'src/main/resources/simulatedlogs/simulator_logs1.log', fileName: 'Simulator1' },
  {value: 'src/main/resources/simulatedlogs/simulator_logs2.log', fileName: 'Simulator2' },
  {value: 'src/main/resources/simulatedlogs/simulator_logs3.log', fileName: 'Simulator3' },
  {value: 'src/main/resources/simulatedlogs/simulator_logs4.log', fileName: 'Simulator4' },
  {value: 'src/main/resources/simulatedlogs/simulator_logs5.log', fileName: 'Simulator5' }]

  formData: FormGroup;

  constructor(private fb: FormBuilder, private logService: LogsService, private _snackBar: MatSnackBar) { 
    this.formData = fb.group({
      'path': ['', Validators.required],
      'duration': ['', Validators.required],
      'regexp': ['',  Validators.required],
      'currentRow': [1]
    })

  }

  ngOnInit(): void {
  }

  addLogConfiguration(){
    this.logService.addLogConfiguration(this.formData.value).toPromise().then( res => {
      this._snackBar.open("Rule successfully added.", "Close");
    }, err => {
      this._snackBar.open("Server error: " + err, "Close");
    })
  }

  typeChanged(value, controlName){
    this.formData.controls[controlName].patchValue(value);
    console.log(this.formData.controls[controlName].value)
  }

}
