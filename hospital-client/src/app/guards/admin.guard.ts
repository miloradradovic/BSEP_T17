import {Injectable} from '@angular/core';
import {Router, CanActivate} from '@angular/router';
import { UserRole } from '../model/log-in.model';
import {LoginService} from '../services/login/login.service';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {

  constructor(
    public auth: LoginService,
    public router: Router
  ) {
  }

  canActivate(): boolean {
    if (this.auth.getRole() !== UserRole.ADMIN) {
      this.router.navigate(['/']);
      return false;
    }
    return true;
    /* if (this.auth.getRole() === 'ROLE_ADMIN') {
      this.router.navigate(['/manage-certificates/request-certificate']);
      return true;
    }
    return false; */
  }
}
