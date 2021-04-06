import {Routes} from '@angular/router';
import {LogInComponent} from './auth/log-in/view/log-in/log-in.component';
import { ManageCertificateComponent } from './features/manage-certificates/manage-certificate.component';
import { AdminGuard } from './guards/admin.guard';
import { LoginGuard } from './guards/login.guard';

export const routes: Routes = [
  {
    path: '', redirectTo: 'login', pathMatch: 'full'
  },
  {
    path: 'login',
    component: LogInComponent
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
