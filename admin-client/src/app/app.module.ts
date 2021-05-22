import {APP_INITIALIZER, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {SharedModule} from './shared/shared.module';
import {NavigationModule} from './navigation/navigation.module';
import {FeaturesModule} from './features/features.module';
import {AuthModule} from './auth/auth.module';
import {RouterModule} from '@angular/router';
import {routes} from './app.routing';
import {HttpAuthInterceptor} from './interceptors/http-auth.interceptor';
import {KeycloakAngularModule, KeycloakService} from 'keycloak-angular';
import {initializer} from './app-init';
import {StorageService} from './service/storage-service/storage.service';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    SharedModule,
    NavigationModule,
    FeaturesModule,
    AuthModule,
    KeycloakAngularModule,
    RouterModule.forRoot(routes)
  ],
  providers: [// {provide: HTTP_INTERCEPTORS, useClass: HttpAuthInterceptor, multi: true},
    {provide: APP_INITIALIZER, useFactory: initializer, multi: true, deps: [KeycloakService, StorageService]}],
  bootstrap: [AppComponent]
})
export class AppModule {
}
