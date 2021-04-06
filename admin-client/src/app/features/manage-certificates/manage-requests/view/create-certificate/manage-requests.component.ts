import { Component, OnInit } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { CertificateRequest } from 'src/app/model/consent-request/certificate-request.model';
import { RequestCertificateService } from 'src/app/service/certificate-requests/certificate-requests.service';

@Component({
  selector: 'app-create-certificate',
  templateUrl: './manage-requests.component.html',
  styleUrls: ['./manage-requests.component.css']
})
export class ManageRequestsComponent implements OnInit {

  dataSource: CertificateRequest[] = [];
  columnsToIterate: string[] = ['commonName','surname','givenName','organization','organizationUnit', 'country','email']
  columnsToDisplay: string[] = ['Common Name','Last Name','First Name','Organization','Organization Unit', 'Country','Email','accept','delete']


  constructor(private requestCertificateService: RequestCertificateService, private spinnerService: NgxSpinnerService) { }

  ngOnInit(): void {
    this.spinnerService.show();
    this.getRequests();
    this.spinnerService.hide();
    console.log(this.dataSource);
  }

  rejectRequest(id: number){
    this.spinnerService.show();
    this.requestCertificateService.rejectRequest(id).toPromise().then(result => {
      console.log('success');  

    }, error => {
      console.log('fail');
    })
    this.getRequests();
    this.spinnerService.hide();
  }

  getRequests(){
    this.requestCertificateService.getCertificateRequests().toPromise().then(result => {
        this.dataSource = result
    })
  }

}
