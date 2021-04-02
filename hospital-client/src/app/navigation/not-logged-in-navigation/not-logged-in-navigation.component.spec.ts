import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotLoggedInNavigationComponent } from './not-logged-in-navigation.component';

describe('NotLoggedInNavigationComponent', () => {
  let component: NotLoggedInNavigationComponent;
  let fixture: ComponentFixture<NotLoggedInNavigationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NotLoggedInNavigationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NotLoggedInNavigationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
