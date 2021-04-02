import {Component, OnInit} from '@angular/core';
import {StorageService} from '../service/storage-service/storage.service';
import {Router} from '@angular/router';
import {LoginService} from '../service/login-service/login.service';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent implements OnInit {

  role: string;

  constructor(private storageService: StorageService,
              private loginService: LoginService,
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

  signOut($event: any): void {
    this.loginService.logOut();
  }

}
