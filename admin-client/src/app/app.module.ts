import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientModule, HTTP_INTERCEPTORS} from '@angular/common/http';
import {SharedModule} from './shared/shared.module';
import {NavigationModule} from './navigation/navigation.module';
import { FeaturesModule } from './features/features.module';
import { AuthModule } from './auth/auth.module';
import { RouterModule } from '@angular/router';
import { routes } from './app.routing';
import { HttpAuthInterceptor } from './interceptors/http-auth.interceptor';

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
    RouterModule.forRoot(routes)
  ],
  providers: [{provide: HTTP_INTERCEPTORS, useClass: HttpAuthInterceptor, multi: true}],
  bootstrap: [AppComponent]
})
export class AppModule {
}
