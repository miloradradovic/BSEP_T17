import {Routes} from '@angular/router';
import { LogsComponent } from './features/logs/logs.component';
// import {LogInComponent} from './auth/log-in/view/log-in/log-in.component';
import { ManageCertificateComponent } from './features/manage-certificates/manage-certificate.component';
import { MessagesComponent } from './features/messages/messages/messages.component';
import { PatientsComponent } from './features/patients/patients/patients.component';
import { ReportComponent } from './features/report/report/report.component';
import { AdminGuard } from './guards/admin.guard';
// import { LoginGuard } from './guards/login.guard';
import {AuthGuard} from './guards/auth.guard';
import { DoctorGuard } from './guards/doctor.guard';

export const routes: Routes = [
  {
    path: '', redirectTo: 'manage-certificates', pathMatch: 'full'
  },
  { 
    path: 'patients', component: PatientsComponent, canActivate: [DoctorGuard]
  },
  { 
    path: 'messages', component: MessagesComponent, canActivate: [DoctorGuard]
  },
  { 
    path: 'logs', component: LogsComponent, canActivate: [AdminGuard]
  },
  { 
    path: 'report', component: ReportComponent, canActivate: [AdminGuard]
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
