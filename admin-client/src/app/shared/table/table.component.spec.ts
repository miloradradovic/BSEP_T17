import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TableComponent } from './table.component';
import {SimpleChange} from '@angular/core';
import {MatTableModule} from '@angular/material/table';

describe('TableComponent', () => {
  let component: TableComponent;
  let fixture: ComponentFixture<TableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [MatTableModule],
      declarations: [ TableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('ngOnChanges()', () => {
    it('should change property value in ngOnChanges lifecycle', () => {

      component.columnsToDisplay = ['one', 'two', 'tree'];
      component.columnsToIterate = ['one', 'two', 'tree'];
      component.ngOnChanges({ columnsToDisplay: new SimpleChange([], ['one', 'two', 'tree'], true),
        columnsToIterate: new SimpleChange([], ['one', 'two', 'tree'], true)});
      fixture.detectChanges();
      expect(component.columnsToDisplay).toEqual(['one', 'two', 'tree']);
      expect(component.columnsToIterate).toEqual(['one', 'two', 'tree']);
    });
  });

  describe('get()', () => {
    it('should get property value', () => {

      const property = component.get('columnsToDisplay');
      fixture.detectChanges();
      expect(property).toEqual([]);
      expect(property).toEqual(component.columnsToDisplay);
    });
  });

  describe('deleted()', () => {
    it('should emit Delete()', () => {

      const spy = spyOn(component.Delete, 'emit');
      component.deleted( 0);
      fixture.detectChanges();
      expect(spy).toHaveBeenCalledWith(0);
    });
  });

  describe('clicked()', () => {
    it('should emit Click()', () => {

      const spy = spyOn(component.Click, 'emit');
      component.clicked( 0);
      fixture.detectChanges();
      expect(spy).toHaveBeenCalledWith(0);
    });
  });

  describe('doubleClicked()', () => {
    it('should emit DoubleClick()', () => {

      const spy = spyOn(component.DoubleClick, 'emit');
      component.doubleClicked( 0);
      fixture.detectChanges();
      expect(spy).toHaveBeenCalledWith(0);
    });
  });
});
