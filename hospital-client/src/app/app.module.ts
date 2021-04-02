import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NavigationComponent } from './navigation/navigation.component';
import { AdminNavigationComponent } from './navigation/admin-navigation/admin-navigation.component';
import { DoctorNavigationComponent } from './navigation/doctor-navigation/doctor-navigation.component';
import { NotLoggedInNavigationComponent } from './navigation/not-logged-in-navigation/not-logged-in-navigation.component';

@NgModule({
  declarations: [
    AppComponent,
    NavigationComponent,
    AdminNavigationComponent,
    DoctorNavigationComponent,
    NotLoggedInNavigationComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
