import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { NgxSpinnerService } from 'ngx-spinner';
import { CertificateRequest } from 'src/app/model/consent-request/certificate-request.model';
import { RequestCertificateService } from 'src/app/service/certificate-requests/certificate-requests.service';
import { AddCertificateComponent } from '../../../add-certificate/add-certificate.component';

@Component({
  selector: 'app-create-certificate',
  templateUrl: './manage-requests.component.html',
  styleUrls: ['./manage-requests.component.css']
})
export class ManageRequestsComponent implements OnInit {

  dataSource: CertificateRequest[] = [];
  columnsToIterate: string[] = ['commonName','surname','givenName','organization','organizationUnit', 'country','email']
  columnsToDisplay: string[] = ['Common Name','Last Name','First Name','Organization','Organization Unit', 'Country','Email','accept','delete']


  constructor(public snackBar: MatSnackBar, private requestCertificateService: RequestCertificateService, private spinnerService: NgxSpinnerService, private dialog: MatDialog) { }

  ngOnInit(): void {
    this.spinnerService.show();
    this.getRequests();
    this.spinnerService.hide();
  }

  acceptRequest(certificate: CertificateRequest): void{
    this.dialog.open(AddCertificateComponent, {
      minWidth: "70vw",
      maxWidth: "90vw",
      minHeight: "60vh",
      maxHeight: "95vh",
      disableClose: true,
      data: {'subjectId': certificate.id }
    }).afterClosed().subscribe(success => {
      if(success){
        this.snackBar.open('Successfully created certificate!', 'Ok', {duration: 2000});
        this.getRequests();
      }
      else{
        this.snackBar.open('Certificate could not be created', 'Ok', {duration: 2000});
      }
    })
  }

  rejectRequest(certificate: CertificateRequest){
    this.spinnerService.show();
    this.requestCertificateService.rejectRequest(certificate.id).toPromise().then(result => {
      this.getRequests();
    }, error => {
      console.log('fail');
    })
    this.spinnerService.hide();
  }

  getRequests(){
    this.requestCertificateService.getCertificateRequests().toPromise().then(result => {
        console.log(result);
        this.dataSource = result
    })
  }



}
