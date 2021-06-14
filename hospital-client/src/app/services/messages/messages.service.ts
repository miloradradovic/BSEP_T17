import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {StorageService} from '../storage/storage.service';
import { PatientModel } from 'src/app/model/patient.model';
import { Message } from 'src/app/model/message.model';

@Injectable({ providedIn: 'root' })
export class MessagesService {

  baseUrl = "https://localhost:8085/patient-status"
  private headers = new HttpHeaders({'Content-Type': 'application/json'});

  constructor(private http: HttpClient, private storageService: StorageService
  ) {
  }

  getMessages= () => this.http.get<Message[]>(this.baseUrl);

  getAlarms= () => this.http.get<Message[]>(this.baseUrl + "/alarm");


}
