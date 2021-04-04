import {Routes} from '@angular/router';
import {LogInComponent} from './auth/log-in/view/log-in/log-in.component';
import {AdminGuard} from './guards/admin.guard';
import {LogInGuard} from './guards/log-in.guard';
import { HomeComponent } from './auth/home/home.component';

export const routes: Routes = [
  {
    path: '',
    component: LogInComponent,
    canActivate: [LogInGuard],
  },
  {
    path: 'manage-certificates',
    loadChildren: () => import('./features/manage-certificates/manage-certificates.module').then(m => m.ManageCertificatesModule)
  },
  {
    path: 'home',
    component: HomeComponent,
    canActivate: [AdminGuard],
  },
];
