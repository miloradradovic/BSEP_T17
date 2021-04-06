import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {StorageService} from '../storage/storage.service';
import { CertificateRequest, PersonModel } from 'src/app/model/certificate-request.model';

@Injectable({ providedIn: 'root' })
export class RequestCertificateService {

  baseUrl = "http://localhost:8085/"
  private headers = new HttpHeaders({'Content-Type': 'application/json'});

  constructor(private http: HttpClient, private storageService: StorageService
  ) {
  }

  getPerson = (personEmail: string) => this.http.get<PersonModel>(this.baseUrl + "user?email=" + personEmail);

  sendCertificateRequest = (certificateRequest: CertificateRequest) => {console.log(certificateRequest);return this.http.post<boolean>(this.baseUrl + "certificate-request", certificateRequest)};

}
