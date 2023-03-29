import { Component, OnInit } from '@angular/core';
import { UserService } from "../../service/user.service";
import { Observable } from "rxjs";
import { Product } from "../../model/product";
import { Location } from "@angular/common";
import { Store } from "@ngrx/store";
import { removeFromBasket, updateBasketProductQuantity } from "../../store/action/user.action";
import { selectBasketProductsState } from "../../store/selector/user.selector";
import { Router } from "@angular/router";

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {
  renewedBasketProducts$: Observable<Product[]>;

  constructor(private readonly userService: UserService, protected readonly location: Location,
              private readonly store: Store, public readonly router: Router) {
  }

  ngOnInit(): void {
    this.renewedBasketProducts$ = this.userService.loadBasketProducts();
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
}
