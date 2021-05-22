import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {StorageService} from '../storage-service/storage.service';
import {LogIn, UserRole} from '../../model/log-in';

@Injectable({
  providedIn: 'root'
})
export class LogInService {
  private headers = new HttpHeaders({'Content-Type': 'application/json'});

  constructor(private http: HttpClient, private storageService: StorageService
  ) {
  }

  logIn(auth: LogIn): Observable<any> {
    return this.http.post('https://localhost:8084/auth/log-in',
      auth, {headers: this.headers, responseType: 'json'});
  }

  logOut(): void {
    this.storageService.clearStorage();
  }

  getRole(): UserRole {
    if (!localStorage.getItem('user')) {
      return UserRole.UNAUTHORIZED;
    }
    return JSON.parse(localStorage.getItem('user')).role === 'SUPER_ADMIN' ? UserRole.SUPER_ADMIN : UserRole.UNAUTHORIZED;
  }
}
