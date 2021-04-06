import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MatSnackBar} from '@angular/material/snack-bar';
import {NgxSpinnerService} from 'ngx-spinner';
import {RequestCertificateService} from 'src/app/services/request-certificate/request-certificate.service';

@Component({
  selector: 'app-request-certificate',
  templateUrl: './request-certificate.component.html',
  styleUrls: ['./request-certificate.component.css']
})
export class RequestCertificateComponent implements OnInit {

  formData: FormGroup;

  constructor(private spinnerService: NgxSpinnerService, private fb: FormBuilder, private certificateService: RequestCertificateService, private _snackBar: MatSnackBar) {
    this.formData = this.fb.group({
      'commonName': null,
      'surname': null,
      'givenName': null,
      'organization': [null, Validators.required],
      'organizationUnit': [null, Validators.required],
      'country': [null, Validators.required],
      'userId': null,
      'email': null,
    });
  }


  ngOnInit(): void {
    this.certificateService.getPerson(JSON.parse(localStorage.getItem('user')).email).toPromise().then(result => {
      this.formData.controls['commonName'].patchValue(JSON.parse(localStorage.getItem('user')).email.split('@')[1]);
      this.formData.controls['surname'].patchValue(result.surname);
      this.formData.controls['givenName'].patchValue(result.name);
    });
  }

  sendRequest() {
    this.formData.controls['userId'].patchValue(JSON.parse(localStorage.getItem('user')).id);
    this.formData.controls['email'].patchValue(JSON.parse(localStorage.getItem('user')).email);
    this.spinnerService.show();
    this.certificateService.sendCertificateRequest(this.formData.value).toPromise().then(result => {
      if (result) {
        this._snackBar.open('Successfully sent certification request.', '', {
          duration: 2000,
        });
      } else {
        this._snackBar.open('An error occurred.', '', {
          duration: 2000,
        });
      }
      this.spinnerService.hide();

    }, reject => {
      this.spinnerService.hide();
      this._snackBar.open('An error occurred.', '', {
        duration: 2000,
      });
    });
  }

}
