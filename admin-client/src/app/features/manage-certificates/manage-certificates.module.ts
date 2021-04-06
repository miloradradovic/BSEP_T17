import {NgModule} from '@angular/core';
import { RouterModule } from '@angular/router';
import { FeaturesModule } from '../features.module';
import { ManageRequestsComponent } from './manage-requests/view/create-certificate/manage-requests.component';
import { ManageCertificateComponent } from './manage-certificate.component';
import { manageCertificateRoutes } from './manage-certificates.routing';
import { MaterialModule } from 'src/app/shared/material.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { AddCertificateComponent } from './add-certificate/add-certificate.component';
import { RevokeDialog, ViewCertificatesComponent } from './view-certificates/view-certificates.component';
import { TreeTableComponent } from './tree-table/tree-table.component';


@NgModule({
  declarations: [ManageRequestsComponent, ManageCertificateComponent, AddCertificateComponent, ViewCertificatesComponent, TreeTableComponent, RevokeDialog],
  imports: [
    FeaturesModule,
    MaterialModule,
    RouterModule.forChild(manageCertificateRoutes),
    SharedModule
  ]
})
export class ManageCertificatesModule {
}
