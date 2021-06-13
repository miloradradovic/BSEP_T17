import {Routes} from '@angular/router';
// import {LogInComponent} from './auth/log-in/view/log-in/log-in.component';
import { ManageCertificateComponent } from './features/manage-certificates/manage-certificate.component';
import { PatientsComponent } from './features/patients/patients/patients.component';
import { AdminGuard } from './guards/admin.guard';
// import { LoginGuard } from './guards/login.guard';
import {AuthGuard} from './guards/auth.guard';

export const routes: Routes = [
  {
    path: '', redirectTo: 'manage-certificates', pathMatch: 'full'
  },
  { 
    path: 'patients', component: PatientsComponent 
  },
  {
    path: 'manage-certificates',
    children: [{
      path: '',
      component: ManageCertificateComponent,
      loadChildren: () => import('./features/manage-certificates/manage-certificates.module').then(m => m.ManageCertificatesModule),
      canActivate: [AuthGuard]
    }]
  },

];
