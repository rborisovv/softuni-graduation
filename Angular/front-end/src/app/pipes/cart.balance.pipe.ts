import { Pipe, PipeTransform } from '@angular/core';
import { Product } from "../interface/product";
import { map, Observable } from "rxjs";

@Pipe({
  name: 'cartBalance'
})
export class CartBalancePipe implements PipeTransform {
  transform(products$: Observable<Product[]>, ...args: unknown[]): Observable<number> {
    return products$.pipe(
      map(x => {
        let totalSum = 0;
        x.forEach(x => totalSum += x.quantity * x.price);
        return totalSum;
      })
    )
  }
}
