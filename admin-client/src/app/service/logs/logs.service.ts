import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { LogModel } from 'src/app/model/logs.model';

@Injectable({providedIn: 'root'})
export class LogsService {

  baseUrl = 'https://localhost:8084/';
  private headers = new HttpHeaders({'Content-Type': 'application/json'});

  constructor(private http: HttpClient) {
  }

  addLogConfiguration = (logConfig: {path: String, duration: number, regexp: String, currentRow: number}) => this.http.post<LogModel[]>(this.baseUrl + 'log-config/send-log-config', logConfig);


}
