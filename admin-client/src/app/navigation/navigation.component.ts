import {Component, OnInit} from '@angular/core';
import {StorageService} from '../service/storage-service/storage.service';
import {Router} from '@angular/router';
import {LogInService} from '../service/log-in-service/log-in.service';
import {UserRole} from '../model/log-in';
import {KeycloakService} from 'keycloak-angular';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent implements OnInit {

  role: UserRole;
  superAdmin: UserRole = UserRole.SUPER_ADMIN;
  unauthorized: UserRole = UserRole.UNAUTHORIZED;

  constructor(private storageService: StorageService,
              private loginService: LogInService,
              private keycloakService: KeycloakService,
              public router: Router) {

    this.storageService.watchStorage().subscribe(() => {
      const user = JSON.parse(sessionStorage.getItem('user'));
      this.role = user ? UserRole.SUPER_ADMIN : UserRole.UNAUTHORIZED;
    });

    const user = JSON.parse(sessionStorage.getItem('user'));
    this.role = user ? UserRole.SUPER_ADMIN : UserRole.UNAUTHORIZED;
  }

  ngOnInit(): void {

  }

  logOut($event: any): void {
    window.location.href = 'https://localhost:8443/auth/realms/admin-portal/protocol/openid-connect/logout?redirect_uri=https://localhost:4200';
  }
}
