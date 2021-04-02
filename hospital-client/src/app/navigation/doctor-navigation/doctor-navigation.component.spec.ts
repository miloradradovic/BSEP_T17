import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoctorNavigationComponent } from './doctor-navigation.component';

describe('DoctorNavigationComponent', () => {
  let component: DoctorNavigationComponent;
  let fixture: ComponentFixture<DoctorNavigationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DoctorNavigationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DoctorNavigationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
