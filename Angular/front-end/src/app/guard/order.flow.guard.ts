import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { OrderService } from "../service/order.service";

@Injectable({
  providedIn: 'root'
})
export class OrderFlowGuard implements CanActivate {
  constructor(private readonly orderService: OrderService) {
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.orderService.canActivateOrderConfirmationFlow();
  }

}
