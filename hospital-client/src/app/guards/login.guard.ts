import {Injectable} from '@angular/core';
import {Router, CanActivate} from '@angular/router';
import { UserRole } from '../model/log-in.model';
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
    if (role === UserRole.ADMIN) {
      this.router.navigate(['/manage-certificates']);
      return false;
    } else if (role === UserRole.DOCTOR) {
      this.router.navigate(['/main-page-doctor']);
      return false;
    }
    this.router.navigate(['']);
    return true;
  }
}
