import {Injectable} from '@angular/core';
import {Router, CanActivate} from '@angular/router';
import { UserRole } from '../model/log-in.model';
import {LoginService} from '../services/login/login.service';

@Injectable({
  providedIn: 'root'
})
export class DoctorGuard implements CanActivate {

  constructor(
    public auth: LoginService,
    public router: Router
  ) {
  }

  canActivate(): boolean {
    console.log(this.auth.getRole());
    if (this.auth.getRole() !== UserRole.DOCTOR) {
      this.router.navigate(['/']);
      return false;
    }
    return true;
  }
}
