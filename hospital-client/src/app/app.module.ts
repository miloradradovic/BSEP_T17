import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {SharedModule} from './shared/shared.module';
import {NavigationModule} from './navigation/navigation.module';
import {MaterialModule} from './shared/material.module';
import {AppRoutingModule} from './app-routing/app-routing.module';
import {LogInModule} from './features/log-in/log-in.module';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    SharedModule,
    NavigationModule,
    MaterialModule,
    AppRoutingModule,
    LogInModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
