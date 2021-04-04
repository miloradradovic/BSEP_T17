import {Injectable} from '@angular/core';
import {CanActivate, Router} from '@angular/router';
import { UserRole } from '../model/log-in';
import {LogInService} from '../service/log-in-service/log-in.service';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {
  constructor(
    public auth: LogInService,
    public router: Router
  ) {
  }

  canActivate(): boolean {
    if (this.auth.getRole() !== UserRole.ROLE_SUPER_ADMIN) {
      this.router.navigate(['/']);
      return false;
    }
    return true;
  }

}
