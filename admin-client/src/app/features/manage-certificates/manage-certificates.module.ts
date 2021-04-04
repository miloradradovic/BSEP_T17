import {NgModule} from '@angular/core';
import { RouterModule } from '@angular/router';
import {MaterialModule} from '../../shared/material.module';
import { CertificateFormComponent } from './create-certificate/container/certificate-form/certificate-form.component';
import { CreateCertificateComponent } from './create-certificate/view/create-certificate/create-certificate.component';
import { manageCertificateRoutes } from './manage-certificates.routing';


@NgModule({
  declarations: [CreateCertificateComponent, CertificateFormComponent],
  imports: [
    MaterialModule,
    RouterModule.forChild(manageCertificateRoutes)
  ]
})
export class ManageCertificatesModule {
}
