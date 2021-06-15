import {NgModule} from '@angular/core';
import { MaterialModule } from '../shared/material.module';
import { LogsComponent } from './logs/logs/logs.component';


@NgModule({
  declarations: [LogsComponent],
  imports: [
    MaterialModule
  ]
})
export class FeaturesModule {
}
