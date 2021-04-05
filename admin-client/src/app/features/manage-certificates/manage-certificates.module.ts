import {NgModule} from '@angular/core';
import { RouterModule } from '@angular/router';
import { FeaturesModule } from '../features.module';
import { CertificateFormComponent } from './create-certificate/container/certificate-form/certificate-form.component';
import { ManageCertificateComponent } from './create-certificate/manage-certificate.component';
import { CreateCertificateComponent } from './create-certificate/view/create-certificate/create-certificate.component';
import { manageCertificateRoutes } from './manage-certificates.routing';


@NgModule({
  declarations: [CreateCertificateComponent, CertificateFormComponent, ManageCertificateComponent],
  imports: [
    FeaturesModule,
    RouterModule.forChild(manageCertificateRoutes)
  ]
})
export class ManageCertificatesModule {
}
