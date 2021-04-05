import {Routes} from '@angular/router';
import {LogInComponent} from './auth/log-in/view/log-in/log-in.component';
import {LoginGuard} from './guards/login.guard';

export const routes: Routes = [
  {
    path: '', redirectTo: 'login', pathMatch: 'full'
  },
  {
    path: 'login',
    component: LogInComponent,
  },

];
