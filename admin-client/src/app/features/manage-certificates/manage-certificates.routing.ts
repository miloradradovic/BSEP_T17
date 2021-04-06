import { Routes } from '@angular/router';
import { ManageRequestsComponent } from './manage-requests/view/create-certificate/manage-requests.component';
import { ViewCertificatesComponent } from './view-certificates/view-certificates.component';

export const manageCertificateRoutes: Routes = [
    {
        path: 'manage-requests',
        component: ManageRequestsComponent
    },
    {
      path: 'view-certificates',
      component: ViewCertificatesComponent
    }
]; 