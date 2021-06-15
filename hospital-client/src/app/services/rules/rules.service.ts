import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {StorageService} from '../storage/storage.service';
import { AdminRule, DoctorRule } from 'src/app/model/rules.model';

@Injectable({ providedIn: 'root' })
export class RulesService {

  baseUrl = "https://localhost:8085/rules"
  private headers = new HttpHeaders({'Content-Type': 'application/json'});

  constructor(private http: HttpClient, private storageService: StorageService
  ) {
  }

  addDoctorRule = (doctorRule: DoctorRule) => this.http.post<any>(this.baseUrl + "/doctor-rule", doctorRule);

  addAdminRule = (adminRule: AdminRule) => this.http.post<any>(this.baseUrl + "/admin-rule", adminRule);

}
