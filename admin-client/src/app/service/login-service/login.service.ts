import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {StorageService} from '../storage-service/storage.service';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private headers = new HttpHeaders({'Content-Type': 'application/json'});

  constructor(private http: HttpClient, private storageService: StorageService
  ) {
  }

  logIn(auth: any): Observable<any> {
    return this.http.post('http://localhost:8080/auth/log-in',
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
