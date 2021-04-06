import {NgModule} from '@angular/core';
import { RouterModule } from '@angular/router';
import { FeaturesModule } from '../features.module';
import { ManageRequestsComponent } from './manage-requests/view/create-certificate/manage-requests.component';
import { ManageCertificateComponent } from './manage-certificate.component';
import { manageCertificateRoutes } from './manage-certificates.routing';
import { MaterialModule } from 'src/app/shared/material.module';
import { SharedModule } from 'src/app/shared/shared.module';


@NgModule({
  declarations: [ManageRequestsComponent, ManageCertificateComponent],
  imports: [
    FeaturesModule,
    MaterialModule,
    RouterModule.forChild(manageCertificateRoutes),
    SharedModule
  ]
})
export class ManageCertificatesModule {
}
