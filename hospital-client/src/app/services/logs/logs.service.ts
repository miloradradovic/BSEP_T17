import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { LogModel, LogsFilterModel } from 'src/app/model/logs.model';

@Injectable({providedIn: 'root'})
export class LogService {

  baseUrl = 'https://localhost:8085/';
  private headers = new HttpHeaders({'Content-Type': 'application/json'});

  constructor(private http: HttpClient
  ) {
  }

  getAllLogs = () => this.http.get<LogModel[]>(this.baseUrl + 'logs/get-logs');

  getAlarmLogs = () => this.http.get<LogModel[]>(this.baseUrl + 'logs/get-logs/alarm');

  filterLogs = (filter: LogsFilterModel) => this.http.post<LogModel[]>(this.baseUrl + 'logs/filter-logs', filter);


}
