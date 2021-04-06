import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Certificate } from 'src/app/model/consent-request/certificate-request.model';
import { RequestCertificateService } from 'src/app/service/certificate-requests/certificate-requests.service';

@Component({
  selector: 'app-view-certificates',
  templateUrl: './view-certificates.component.html',
  styleUrls: ['./view-certificates.component.css']
})
export class ViewCertificatesComponent implements OnInit {

  constructor(private snackBar: MatSnackBar, private dialog: MatDialog, private certificateService: RequestCertificateService) { }

  dataSource: Certificate[] = [];
  columnsToDisplay = ['Common name', 'Full name', 'Email', 'Revoked', 'Revocation reason', 'CA', 'delete'];
  columnsToIterate = ['commonName', 'fullName', 'email', 'revoked', 'revocationReason', 'ca'];

  ngOnInit(): void {
    this.getCertificates();
  }

  getCertificates(){
    this.certificateService.getCertificates().toPromise().then(result=>{
       this.dataSource = [result];
    })
  }

  revoke(certificate: Certificate){
      event.stopPropagation();
      const dialogRef = this.dialog.open(RevokeDialog, {
        width: '600px'
      });
  
      dialogRef.afterClosed().subscribe(result => {
        if(result){
          this.certificateService.revokeCertificate({subjectAlias: certificate.alias, revocationReason: result}).toPromise().then( result => {
            this.snackBar.open('Successfully revoked certificate!', 'Ok', {duration: 2000});
            this.getCertificates();
          }, err => {
            this.snackBar.open('Sertificate revocation failed!', 'Ok', {duration: 2000});
          })
        }
      });
    }
  
}

@Component({
  selector: 'dialog-overview-example-dialog',
  template: `<mat-card>
    <mat-card-title style="margin-bottom: 2%">Enter revocation reason</mat-card-title>
    <mat-card-content class="full-height">
    <mat-form-field appearance="fill">
    <mat-label>Reason</mat-label>
      <mat-select [(ngModel)]="revokeMessage">
        <mat-option value="UNSPECIFIED">UNSPECIFIED</mat-option>
        <mat-option value="KEY_COMPROMISE">KEY_COMPROMISE</mat-option>
        <mat-option value="CA_COMPROMISE">CA_COMPROMISE</mat-option>
        <mat-option value="AFFILIATION_CHANGED">AFFILIATION_CHANGED</mat-option>
        <mat-option value="SUPERSEDED">SUPERSEDED</mat-option>
        <mat-option value="CESSATION_OF_OPERATION">CESSATION_OF_OPERATION</mat-option>
        <mat-option value="CERTIFICATE_HOLD">CERTIFICATE_HOLD</mat-option>
        <mat-option value="REMOVE_FROM_CRL">REMOVE_FROM_CRL</mat-option>
        <mat-option value="PRIVILEGE_WITHDRAWN">PRIVILEGE_WITHDRAWN</mat-option>
        <mat-option value="AA_COMPROMISE">AA_COMPROMISE</mat-option>
      </mat-select>
    </mat-form-field>
    </mat-card-content>
    <div fxLayout="row">
      <div fxFlex="10">
        <button (click)="onNoClick()" mat-raised-button color="warn">Cancel</button>
      </div>
      <div fxFlex="79"></div>
      <div fxFlex="10">
        <button (click)="sendRequest()" [disabled]="!revokeMessage"  mat-raised-button color="primary">Send</button>
      </div>

    </div>

  </mat-card>`,
})
export class RevokeDialog {

  revokeMessage: string = "UNSPECIFIED";

  constructor(
    public dialogRef: MatDialogRef<RevokeDialog>,
    @Inject(MAT_DIALOG_DATA) public data: string) {}

  onNoClick(): void {
    this.dialogRef.close();
  }

  sendRequest(){
    this.dialogRef.close(this.revokeMessage);
  }
}
