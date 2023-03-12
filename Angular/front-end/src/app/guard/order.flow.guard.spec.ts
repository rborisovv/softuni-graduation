import { TestBed } from '@angular/core/testing';

import { OrderFlowGuard } from './order.flow.guard';

describe('OrderFlowGuard', () => {
  let guard: OrderFlowGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(OrderFlowGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
