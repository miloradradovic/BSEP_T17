import { NgModule } from '@angular/core';
import { MaterialModule } from '../shared/material.module';
import { ManageCertificateComponent } from './manage-certificates/manage-certificate.component';
import { ManageCertificatesModule } from './manage-certificates/manage-certificates.module';
import { RequestCertificateComponent } from './manage-certificates/request-certificate/request-certificate.component';
import { PatientsComponent } from './patients/patients/patients.component';
import { MessagesComponent } from './messages/messages/messages.component';
import { LogsComponent } from './logs/logs.component';
import { ReportComponent } from './report/report/report.component';


@NgModule({
  declarations: [PatientsComponent, MessagesComponent, LogsComponent, ReportComponent],
  imports: [
    MaterialModule
  ]
})
export class FeaturesModule { }
