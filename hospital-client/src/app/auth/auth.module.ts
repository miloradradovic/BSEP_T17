import {NgModule} from '@angular/core';
import { MaterialModule } from '../shared/material.module';
import { LogInComponent } from './log-in/view/log-in/log-in.component';


@NgModule({
  declarations: [LogInComponent],
  imports: [
    MaterialModule
  ]
})
export class AuthModule {
}
