import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { CertificateRequest } from 'src/app/model/consent-request/certificate-request.model';

@Injectable({ providedIn: 'root' })
export class RequestCertificateService {

  baseUrl = "http://localhost:8080/"
  private headers = new HttpHeaders({'Content-Type': 'application/json'});

  constructor(private http: HttpClient
  ) {
  }

  getCertificateRequests = () => this.http.get<CertificateRequest[]>(this.baseUrl + "certificate-request");

  acceptRequest = () => this.http.get<CertificateRequest[]>(this.baseUrl + "certificate-request");

  rejectRequest = (id: number) => this.http.delete<void>(this.baseUrl + "certificate-request/" + id);

}
