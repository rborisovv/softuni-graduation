import { Component, OnInit } from '@angular/core';
import { map, Observable, of, take } from "rxjs";
import { Product } from "../../model/product";
import { Router } from "@angular/router";
import { Store } from "@ngrx/store";
import { Location } from "@angular/common";
import { UserService } from "../../service/user.service";
import { Checkout } from "../../model/checkout";
import { createOrder } from "../../store/action/user.action";
import { Voucher } from "../../model/voucher";
import { selectDiscountedTotalState } from "../../store/selector/basket.selector";
import { BasketService } from "../../service/basket.service";

@Component({
  selector: 'app-finalize',
  templateUrl: './finalize.component.html',
  styleUrls: ['./finalize.component.scss']
})
export class FinalizeComponent implements OnInit {
  checkoutData: Checkout;
  discountedBasketTotal$: Observable<string> = of(undefined);
  voucher$: Observable<Voucher> = of(undefined);

  constructor(private store: Store, protected router: Router, protected location: Location,
              private userService: UserService, private basketService: BasketService) {
  }

  renewedBasketProducts$: Observable<Product[]>;

  ngOnInit(): void {
    this.renewedBasketProducts$ = this.userService.loadBasketProducts();
    this.userService.fetchCheckoutDataIfPresent().pipe(take(1))
      .subscribe(data => this.checkoutData = data);

    this.voucher$ = this.basketService.fetchVoucherIfPresent();

    this.discountedBasketTotal$ = this.store.select(selectDiscountedTotalState).pipe(
      map(state => state ? (state.total <= 0 ? 'Free' : (state.total.toFixed(2) + ' lv.')) : undefined)
    );
  }

  createOrder() {
    this.store.dispatch(createOrder());
  }
}
