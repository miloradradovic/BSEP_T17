import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotLoggedinNavigationComponent } from './not-loggedin-navigation.component';

describe('NotLoggedinNavigationComponent', () => {
  let component: NotLoggedinNavigationComponent;
  let fixture: ComponentFixture<NotLoggedinNavigationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NotLoggedinNavigationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NotLoggedinNavigationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
