import {Injectable} from '@angular/core';
import {CanActivate, Router} from '@angular/router';
import { UserRole } from '../model/log-in';
import {LogInService} from '../service/log-in-service/log-in.service';

@Injectable({
  providedIn: 'root'
})
export class LogInGuard implements CanActivate {

  constructor(
    public auth: LogInService,
    public router: Router
  ) {
  }

  canActivate(): boolean {
    const role = this.auth.getRole();
    if (role === UserRole.ROLE_SUPER_ADMIN) {
      this.router.navigate(['/home']);
      return false;
    }
    return true;
  }
}
