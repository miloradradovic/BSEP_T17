import {NgModule} from '@angular/core';
import {LogInComponent} from './view/log-in/log-in.component';
import {MaterialModule} from '../../shared/material.module';


@NgModule({
  declarations: [LogInComponent],
  imports: [
    MaterialModule
  ]
})
export class LogInModule {
}
