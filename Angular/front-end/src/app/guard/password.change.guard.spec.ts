import { TestBed } from '@angular/core/testing';

import { PasswordChangeGuard } from './password.change.guard';

describe('PasswordChangeGuard', () => {
  let guard: PasswordChangeGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(PasswordChangeGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
