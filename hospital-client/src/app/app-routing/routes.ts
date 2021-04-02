import {Routes} from '@angular/router';
import {LogInComponent} from '../features/log-in/view/log-in/log-in.component';
import {LoginGuard} from '../guards/login.guard';

export const routes: Routes = [
  {
    path: '',
    component: LogInComponent,
    canActivate: [LoginGuard]
  },
  {
    path: '**',
    redirectTo: ''
  }
];
