import {Pipe, PipeTransform} from '@angular/core';
import {Product} from "../interface/product";
import {map, Observable} from "rxjs";

@Pipe({
  name: 'cartBalance'
})
export class CartBalancePipe implements PipeTransform {
  transform(value: Observable<Product[]>, ...args: unknown[]): Observable<number> {
    return value.pipe(
      map(x => {
        let totalSum = 0;
        x.forEach(x => totalSum += x.price);
        return totalSum;
      })
    )
  }
}
