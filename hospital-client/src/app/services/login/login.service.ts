import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {StorageService} from '../storage/storage.service';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private headers = new HttpHeaders({'Content-Type': 'application/json'});

  constructor(private http: HttpClient, private storageService: StorageService
  ) {
  }

  logIn(auth: any): Observable<any> {
    return this.http.post('http://localhost:8085/auth/log-in',
      auth, {headers: this.headers, responseType: 'text'});
  }

  logOut(): void {
    this.storageService.clearStorage();
  }

  getRole(): string {
    if (!localStorage.getItem('user')) {
      return '';
    }
    return JSON.parse(localStorage.getItem('user')).role;
  }
}
