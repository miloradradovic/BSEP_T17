import {Routes} from '@angular/router';
import {LogInComponent} from '../features/log-in/view/log-in/log-in.component';
import {AdminGuard} from '../guards/admin.guard';
import {LogInGuard} from '../guards/log-in.guard';

export const routes: Routes = [
  {
    path: '',
    component: LogInComponent,
    canActivate: [LogInGuard]
  },
  {
    path: 'view-requests',
    component: LogInComponent,
    canActivate: [AdminGuard]
  },
  {
    path: '**',
    redirectTo: ''
  }
];
