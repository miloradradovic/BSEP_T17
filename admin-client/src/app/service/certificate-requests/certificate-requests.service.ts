import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Certificate, CertificateCreation, CertificateRequest } from 'src/app/model/consent-request/certificate-request.model';

@Injectable({ providedIn: 'root' })
export class RequestCertificateService {

  baseUrl = "http://localhost:8080/"
  private headers = new HttpHeaders({'Content-Type': 'application/json'});

  constructor(private http: HttpClient
  ) {
  }

  getCertificateRequests = () => this.http.get<CertificateRequest[]>(this.baseUrl + "certificate-request");

  getCertificates = () => this.http.get<Certificate>(this.baseUrl + "certificate");

  acceptRequest = (createViewModel: CertificateCreation) => this.http.post<void>(this.baseUrl + "certificate", createViewModel);

  rejectRequest = (id: number) => this.http.delete<void>(this.baseUrl + "certificate-request/" + id);

  revokeCertificate = (revokation: {subjectAlias: string, revocationReason: string}) => this.http.put<void>(this.baseUrl + "certificate/", revokation);

}
