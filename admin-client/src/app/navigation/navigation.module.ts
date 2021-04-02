import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {NavigationComponent} from './navigation.component';
import {NotLoggedInNavigationComponent} from './not-logged-in-navigation/not-logged-in-navigation.component';
import {AdminNavigationComponent} from './admin-navigation/admin-navigation.component';
import {RouterModule} from '@angular/router';
import {MatIconModule} from '@angular/material/icon';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatButtonModule} from '@angular/material/button';


@NgModule({
  declarations: [
    NavigationComponent,
    NotLoggedInNavigationComponent,
    AdminNavigationComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    MatIconModule,
    MatToolbarModule,
    MatButtonModule
  ],
  exports: [
    NavigationComponent,
    NotLoggedInNavigationComponent,
    AdminNavigationComponent
  ]
})
export class NavigationModule {
}
