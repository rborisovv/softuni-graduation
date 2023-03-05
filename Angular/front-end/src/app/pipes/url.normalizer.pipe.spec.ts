import { UrlNormalizerPipe } from './url.normalizer.pipe';

describe('UrlNormalizerPipe', () => {
  it('create an instance', () => {
    const pipe = new UrlNormalizerPipe();
    expect(pipe).toBeTruthy();
  });
});
