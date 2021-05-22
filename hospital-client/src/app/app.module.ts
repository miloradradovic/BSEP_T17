import {APP_INITIALIZER, NgModule} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { SharedModule } from './shared/shared.module';
import { NavigationModule } from './navigation/navigation.module';
import { MaterialModule } from './shared/material.module';
import { FeaturesModule } from './features/features.module';
import { RouterModule } from '@angular/router';
import { routes } from './routes';
import { AuthModule } from './auth/auth.module';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { HttpAuthInterceptor } from './interceptors/http.auth.interceptor';
import {KeycloakAngularModule, KeycloakService} from 'keycloak-angular';
import {initializer} from './app-init';
import {StorageService} from './services/storage/storage.service';


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
    FeaturesModule,
    AuthModule,
    KeycloakAngularModule,
    RouterModule.forRoot(routes)
  ],
  providers: [// {provide: HTTP_INTERCEPTORS, useClass: HttpAuthInterceptor, multi: true}
    {provide: APP_INITIALIZER, useFactory: initializer, multi: true, deps: [KeycloakService, StorageService]}],
  bootstrap: [AppComponent]
})
export class AppModule { }
