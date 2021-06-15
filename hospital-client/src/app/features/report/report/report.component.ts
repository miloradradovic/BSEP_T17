import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ReportModel } from 'src/app/model/report.model';
import { ReportService } from 'src/app/services/report/report.service';

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.css']
})
export class ReportComponent implements OnInit {

  reportData: ReportModel;
  formData: FormGroup;

  constructor(private reportService: ReportService, private fb: FormBuilder) { 
    this.formData = this.fb.group ({
      'from': [null],
      'to': [null]
    })
  }

  ngOnInit(): void {

  }
  
  getReport(){
    this.reportService.getReport(this.formData.value).toPromise().then( res => {
      this.reportData = res;
      console.log(this.reportData);
    })

  }


}
