import { CartBalancePipe } from './cart.balance.pipe';

describe('CartBalancePipe', () => {
  it('create an instance', () => {
    const pipe = new CartBalancePipe();
    expect(pipe).toBeTruthy();
  });
});
