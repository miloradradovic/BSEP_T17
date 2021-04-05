import {NgModule} from '@angular/core';
import {TableComponent} from './table/table.component';
import {PaginationComponent} from './pagination/pagination.component';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MaterialModule} from './material.module';

@NgModule({
  declarations: [TableComponent, PaginationComponent],
  imports: [
    MatPaginatorModule,
    MaterialModule
  ],
  exports: [
    TableComponent, PaginationComponent
  ],
  providers: []
})
export class SharedModule { }
