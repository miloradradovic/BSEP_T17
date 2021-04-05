import {Routes} from '@angular/router';
import {LogInComponent} from './auth/log-in/view/log-in/log-in.component';
import {LogInGuard} from './guards/log-in.guard';
import { ManageCertificateComponent } from './features/manage-certificates/create-certificate/manage-certificate.component';

export const routes: Routes = [
  {
    path: '', redirectTo: 'login', pathMatch: 'full'
  },
  {
    path: 'login',
    component: LogInComponent,
    canActivate: [LogInGuard],
  },
  {
    path: 'manage-certificates',
    children: [{
      path: '',
      component: ManageCertificateComponent,
      loadChildren: () => import('./features/manage-certificates/manage-certificates.module').then(m => m.ManageCertificatesModule)
    }]
  },
  
];
