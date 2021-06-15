import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { ReportModel } from 'src/app/model/report.model';

@Injectable({providedIn: 'root'})
export class ReportService {

  baseUrl = 'https://localhost:8085/';
  private headers = new HttpHeaders({'Content-Type': 'application/json'});

  constructor(private http: HttpClient) {
  }

  getReport = (reportParams: {from: Date, to: Date}) => this.http.post<ReportModel>(this.baseUrl + 'log-report', reportParams);
}
