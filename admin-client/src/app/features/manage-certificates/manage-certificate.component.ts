import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component, OnInit } from '@angular/core';
import { RequestCertificateService } from 'src/app/service/certificate-requests/certificate-requests.service';

@Component({
  selector: 'app-certificate-form',
  templateUrl: './manage-certificate.component.html',

})
export class ManageCertificateComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }
}

