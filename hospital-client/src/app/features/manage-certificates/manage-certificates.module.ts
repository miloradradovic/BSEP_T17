import {NgModule} from '@angular/core';
import { RouterModule } from '@angular/router';
import { MaterialModule } from 'src/app/shared/material.module';
import { FeaturesModule } from '../features.module';
import { ManageCertificateComponent } from './manage-certificate.component';
import { manageCertificateRoutes } from './manage-certificates.routing';
import { RequestCertificateComponent } from './request-certificate/request-certificate.component';



@NgModule({
  declarations: [RequestCertificateComponent, ManageCertificateComponent],
  imports: [
    FeaturesModule,
    MaterialModule,
    RouterModule.forChild(manageCertificateRoutes)
  ]
})
export class ManageCertificatesModule {
}
