import {NgModule} from '@angular/core';
// import {LogInComponent} from './log-in/view/log-in/log-in.component';
import {MaterialModule} from '../shared/material.module';
import { HomeComponent } from './home/home.component';
import { RouterModule } from '@angular/router';


@NgModule({
  declarations: [HomeComponent],
  imports: [
    MaterialModule,
    RouterModule
  ]
})
export class AuthModule {
}
