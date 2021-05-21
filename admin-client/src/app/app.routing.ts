import {Routes} from '@angular/router';
// import {LogInComponent} from './auth/log-in/view/log-in/log-in.component';
import {ManageCertificateComponent} from './features/manage-certificates/manage-certificate.component';
import {AuthGuard} from './guards/auth.guard';
// import {LogInGuard} from './guards/log-in.guard';

export const routes: Routes = [
  {
    path: '', redirectTo: 'manage-certificates', pathMatch: 'full'
  },
  /*{
    path: 'login',
    component: LogInComponent,
    canActivate: [LogInGuard],
  },*/
  {
    path: 'manage-certificates',
    children: [{
      path: '',
      component: ManageCertificateComponent,
      loadChildren: () => import('./features/manage-certificates/manage-certificates.module').then(m => m.ManageCertificatesModule)
      , canActivate: [AuthGuard]}]
  }

];
