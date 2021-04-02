import { TestBed } from '@angular/core/testing';

import { LogInService } from './log-in.service';

describe('LoginService', () => {
  let service: LogInService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LogInService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
