import {NgModule} from '@angular/core';
import {MaterialModule} from '../shared/material.module';
import { ManageCertificatesModule } from './manage-certificates/manage-certificates.module';


@NgModule({
  declarations: [],
  imports: [
    MaterialModule,
    ManageCertificatesModule,
  ]
})
export class FeaturesModule {
}
