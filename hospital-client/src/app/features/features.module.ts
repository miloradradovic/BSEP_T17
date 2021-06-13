import { NgModule } from '@angular/core';
import { MaterialModule } from '../shared/material.module';
import { ManageCertificateComponent } from './manage-certificates/manage-certificate.component';
import { ManageCertificatesModule } from './manage-certificates/manage-certificates.module';
import { RequestCertificateComponent } from './manage-certificates/request-certificate/request-certificate.component';
import { PatientsComponent } from './patients/patients/patients.component';


@NgModule({
  declarations: [PatientsComponent],
  imports: [
    MaterialModule
  ]
})
export class FeaturesModule { }
