import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { LogModel } from 'src/app/model/logs.model';

@Injectable({providedIn: 'root'})
export class LogsService {

  baseUrl = 'https://localhost:8084/';
  private headers = new HttpHeaders({'Content-Type': 'application/json'});

  constructor(private http: HttpClient
  ) {
  }

  getCertificateRequests = () => this.http.get<LogModel[]>(this.baseUrl + 'logs/get-logs');

  //acceptRequest = (createViewModel: CertificateCreation) => this.http.post<void>(this.baseUrl + 'certificate', createViewModel);

}
