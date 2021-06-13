import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { PatientModel } from 'src/app/model/patient.model';
import { PatientService } from 'src/app/services/patient/patient.service';


@Component({
  selector: 'app-patients',
  templateUrl: './patients.component.html',
  styleUrls: ['./patients.component.css']
})
export class PatientsComponent implements OnInit {

  displayedColumns: string[] = ['name', 'surname', 'dateOfBirth', 'bloodType', 'averageHearthBeat', 'averagePressure', 'averageTemperature'];
  dataSource: MatTableDataSource<PatientModel>;
  @ViewChild(MatPaginator) paginator: MatPaginator;


  constructor(private patientService: PatientService) { 

      this.patientService.getPetients().toPromise().then( res => {
        this.dataSource = new MatTableDataSource<PatientModel>(res);
        this.dataSource.paginator = this.paginator;
      })

  }

  ngOnInit(): void {
  }

}
