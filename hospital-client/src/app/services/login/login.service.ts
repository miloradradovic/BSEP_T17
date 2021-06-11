import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {StorageService} from '../storage/storage.service';
import {Observable} from 'rxjs';
import { UserRole } from 'src/app/model/log-in.model';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private headers = new HttpHeaders({'Content-Type': 'application/json'});

  constructor(private http: HttpClient, private storageService: StorageService
  ) {
  }

  logIn(auth: any): Observable<any> {
    return this.http.post('https://localhost:8085/auth/log-in',
      auth, {headers: this.headers, responseType: 'json'});
  }

  logOut(): void {
    this.storageService.clearStorage();
  }

  getRole(): UserRole {
    if (!sessionStorage.getItem('user')) {
      return UserRole.UNAUTHORIZED;
    }
    return JSON.parse(sessionStorage.getItem('user')).role === "ADMIN" ? UserRole.ADMIN : UserRole.DOCTOR;
  }
}
