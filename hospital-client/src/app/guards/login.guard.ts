import {Injectable} from '@angular/core';
import {Router, CanActivate} from '@angular/router';
import {LoginService} from '../services/login/login.service';

@Injectable({
  providedIn: 'root'
})
export class LoginGuard implements CanActivate {

  constructor(
    public auth: LoginService,
    public router: Router
  ) {
  }

  canActivate(): boolean {
    const role = this.auth.getRole();
    if (role === 'ROLE_ADMIN') {
      this.router.navigate(['/create-request']);
      return false;
    } else if (role === 'ROLE_DOCTOR') {
      this.router.navigate(['/main-page-doctor']);
      return false;
    }
    return true;
  }
}
