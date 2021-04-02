import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientModule} from '@angular/common/http';
import {AppRoutingModule} from './app-routing/app-routing.module';
import {SharedModule} from './shared/shared.module';
import {NavigationModule} from './navigation/navigation.module';
import {LogInModule} from './features/log-in/log-in.module';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    AppRoutingModule,
    SharedModule,
    NavigationModule,
    LogInModule

  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
