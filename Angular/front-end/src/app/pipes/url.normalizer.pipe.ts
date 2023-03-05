import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'urlNormalizer'
})
export class UrlNormalizerPipe implements PipeTransform {

  transform(url: any, ...args: unknown[]): unknown {
    return url.toString().toLowerCase().trim().replaceAll(' ', '-');
  }
}
