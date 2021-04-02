import {Injectable} from '@angular/core';
import {CanActivate, Router} from '@angular/router';
import {LoginService} from '../service/login-service/login.service';

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
    if (this.auth.getRole() !== 'ROLE_SUPER_ADMIN') {
      this.router.navigate(['/']);
      return false;
    }
    return true;
  }

}
