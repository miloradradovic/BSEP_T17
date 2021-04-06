import { Component, OnInit } from '@angular/core';
import { ActivationStart, NavigationEnd, Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
 
  title = 'admin-client';

  constructor(private router: Router){

  }

  ngOnInit(): void {
  }

}
