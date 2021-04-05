import {NgModule} from '@angular/core';
import {NavigationComponent} from './navigation.component';
import {MaterialModule} from '../shared/material.module';
import {AdminNavigationComponent} from './admin-navigation/admin-navigation.component';
import {DoctorNavigationComponent} from './doctor-navigation/doctor-navigation.component';
import {NotLoggedInNavigationComponent} from './not-logged-in-navigation/not-logged-in-navigation.component';

@NgModule({
  declarations: [
    NavigationComponent,
    AdminNavigationComponent,
    DoctorNavigationComponent,
    NotLoggedInNavigationComponent
  ],
  imports: [
    MaterialModule
  ],
  exports: [
    NavigationComponent,
    AdminNavigationComponent,
    DoctorNavigationComponent,
    NotLoggedInNavigationComponent
  ],
  providers: []
})
export class NavigationModule { }
