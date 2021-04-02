import {Injectable} from '@angular/core';
import {CanActivate, Router} from '@angular/router';
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
    if (this.auth.getRole() !== 'ROLE_SUPER_ADMIN') {
      this.router.navigate(['/']);
      return false;
    }
    return true;
  }

}
