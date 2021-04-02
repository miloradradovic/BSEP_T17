import {Injectable} from '@angular/core';
import {CanActivate, Router} from '@angular/router';
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
    if (role === 'ROLE_SUPER_ADMIN') {
      this.router.navigate(['/view-requests']);
      return false;
    }
    return true;
  }
}
