import { Routes } from '@angular/router';
import { RequestCertificateComponent } from './request-certificate/request-certificate.component';

export const manageCertificateRoutes: Routes = [
    {
        path: 'request-certificate',
        component: RequestCertificateComponent,
    },
]; 