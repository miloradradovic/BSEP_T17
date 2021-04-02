import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';
import {routes} from './routes';
import {NgModule} from '@angular/core';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule {
}
