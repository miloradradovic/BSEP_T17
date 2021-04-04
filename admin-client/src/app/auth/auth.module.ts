import {NgModule} from '@angular/core';
import {LogInComponent} from './log-in/view/log-in/log-in.component';
import {MaterialModule} from '../shared/material.module';
import { HomeComponent } from './home/home.component';


@NgModule({
  declarations: [LogInComponent, HomeComponent],
  imports: [
    MaterialModule
  ]
})
export class AuthModule {
}
