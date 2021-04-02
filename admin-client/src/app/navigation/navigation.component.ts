import {Component, OnInit} from '@angular/core';
import {StorageService} from '../service/storage-service/storage.service';
import {Router} from '@angular/router';
import {LogInService} from '../service/log-in-service/log-in.service';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent implements OnInit {

  role: string;

  constructor(private storageService: StorageService,
              private loginService: LogInService,
              public router: Router) {
  }

  ngOnInit(): void {
    this.storageService.watchStorage().subscribe(() => {
      const user = JSON.parse(localStorage.getItem('user'));
      if (user === null) {
        this.role = '';
      } else {
        this.role = user.role;
      }
    });

    const user = JSON.parse(localStorage.getItem('user'));
    if (user === null) {
      this.role = '';
    } else {
      this.role = user.role;
    }
  }

  logOut($event: any): void {
    this.loginService.logOut();
  }

}
