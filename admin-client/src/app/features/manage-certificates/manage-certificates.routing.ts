import { Routes } from '@angular/router';
import { CreateCertificateComponent } from './create-certificate/view/create-certificate/create-certificate.component';

export const manageCertificateRoutes: Routes = [
    {
        path: 'create-certificate',
        component: CreateCertificateComponent,
    },
];