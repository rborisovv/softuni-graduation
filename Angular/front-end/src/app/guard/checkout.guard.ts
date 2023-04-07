import { Injectable } from '@angular/core';
import { UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { BasketService } from "../service/basket.service";

@Injectable()
export class CheckoutGuard {
  constructor(private readonly basketService: BasketService) {
  }

  canActivate(): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.basketService.canActivateCheckout();
  }

}
