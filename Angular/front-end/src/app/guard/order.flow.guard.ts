import { Injectable } from '@angular/core';
import { Router, UrlTree } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { OrderService } from "../service/order.service";

@Injectable()
export class OrderFlowGuard {
  constructor(private orderService: OrderService, private router: Router) {
  }

  canActivate(): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.orderService.canActivateOrderConfirmationFlow().pipe(tap((response) => {
      if (response == false) {
        this.router.navigateByUrl('/home');
      }
    }));
  }
}
