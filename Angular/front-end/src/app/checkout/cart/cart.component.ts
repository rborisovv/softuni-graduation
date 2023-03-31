import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { UserService } from "../../service/user.service";
import { Observable, take } from "rxjs";
import { Product } from "../../model/product";
import { Location } from "@angular/common";
import { Store } from "@ngrx/store";
import { removeFromBasket, updateBasketProductQuantity } from "../../store/action/user.action";
import { selectBasketProductsState } from "../../store/selector/user.selector";
import { Router } from "@angular/router";
import { BasketService } from "../../service/basket.service";
import { Voucher } from "../../model/voucher";

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {
  renewedBasketProducts$: Observable<Product[]>;
  voucher$: Observable<Voucher | null>;

  totalAfterDiscount: Observable<number>;

  @ViewChild('totalPrice') totalPrice: ElementRef | undefined;

  constructor(private userService: UserService, protected location: Location,
              private store: Store, public router: Router, private basketService: BasketService) {
  }

  ngOnInit(): void {
    this.renewedBasketProducts$ = this.userService.loadBasketProducts();
    // this.voucher$ = this.basketService.fetchVoucherIfPresent().pipe(take(1), tap((voucher) => {
    //   if (voucher) {
    //     this.totalAfterVoucherDiscount = this.cartBalance.transform(this.renewedBasketProducts$)
    //       .pipe(take(1), switchMap(total => {
    //         if (voucher.type === 'RELATIVE') {
    //           return of(total - (voucher.discount / 100) * total);
    //         }
    //
    //         return of(total - voucher.discount);
    //       }));
    //   }
    // }), switchMap(voucher => of(voucher)));
  }

  removeProductFromBasket(identifier: string) {
    this.store.dispatch(removeFromBasket({ identifier }));
    this.renewedBasketProducts$ = this.store.select(selectBasketProductsState);
  }

  changeQuantityForProduct(identifier: string, event: Event) {
    const quantity = +(<HTMLSelectElement>event.target).value;
    this.store.dispatch(updateBasketProductQuantity({ identifier: identifier, quantity: quantity }));
    this.renewedBasketProducts$ = this.store.select(selectBasketProductsState);
  }

  addVoucher(labelElement: HTMLInputElement) {
    const voucher: string = labelElement.value;
    this.voucher$ = this.basketService.addVoucherToBasket(voucher).pipe(take(1));
  }
}
