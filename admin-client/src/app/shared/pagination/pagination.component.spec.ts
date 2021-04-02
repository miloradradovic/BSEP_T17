import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PaginationComponent } from './pagination.component';
import {MatPaginatorModule} from '@angular/material/paginator';
import {SimpleChange} from '@angular/core';

describe('PaginationComponent', () => {
  let component: PaginationComponent;
  let fixture: ComponentFixture<PaginationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PaginationComponent ],
      imports: [MatPaginatorModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PaginationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('ngOnChanges()', () => {
    it('should change property value in ngOnChanges lifecycle', () => {

      component.pageSize = 1;
      component.ngOnChanges({ pageSize: new SimpleChange(0, 1, true) });
      fixture.detectChanges();
      expect(component.pageSize).toEqual(1);
    });
  });

  describe('get()', () => {
    it('should get property value', () => {

      const property = component.get('pageSize');
      fixture.detectChanges();
      expect(property).toEqual(0);
      expect(property).toEqual(component.pageSize);
    });
  });

  describe('handlePage()', () => {
    it('should emit ChangePage()', () => {

      const spy = spyOn(component.ChangePage, 'emit');
      component.handlePage({pageIndex: 0});
      fixture.detectChanges();
      expect(spy).toHaveBeenCalledWith(0);
    });
  });
});
