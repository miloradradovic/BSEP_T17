import {NgModule} from '@angular/core';
import {TableComponent} from './table/table.component';
import {PaginationComponent} from './pagination/pagination.component';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatTableModule} from '@angular/material/table';
import {MaterialModule} from './material.module';

@NgModule({
  declarations: [TableComponent, PaginationComponent],
  imports: [
    MatPaginatorModule,
    MatTableModule,
    MaterialModule
  ],
  exports: [
    TableComponent, PaginationComponent
  ],
  providers: []
})
export class SharedModule {
}
