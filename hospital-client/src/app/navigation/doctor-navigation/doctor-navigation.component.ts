import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-doctor-navigation',
  templateUrl: './doctor-navigation.component.html',
  styleUrls: ['./doctor-navigation.component.css']
})
export class DoctorNavigationComponent implements OnInit {

  @Output() logOut = new EventEmitter<void>();

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  logOutUser(): void {
    this.logOut.emit();
  }

}
