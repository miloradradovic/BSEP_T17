import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {StorageService} from '../storage/storage.service';
import { PatientModel } from 'src/app/model/patient.model';

@Injectable({ providedIn: 'root' })
export class PatientService {

  baseUrl = "https://localhost:8085/patient"
  private headers = new HttpHeaders({'Content-Type': 'application/json'});

  constructor(private http: HttpClient, private storageService: StorageService
  ) {
  }

  getPetients = () => this.http.get<PatientModel[]>(this.baseUrl);


}
