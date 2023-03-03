import {Component, OnInit} from '@angular/core';
import {UserService} from "../../service/user.service";
import {Observable} from "rxjs";
import {Product} from "../../interface/product";
import {Location} from "@angular/common";
import {Store} from "@ngrx/store";
import {removeFromBasket} from "../../store/action/user.action";
import {selectBasketProductsState} from "../../store/selector/user.selector";

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {
  loadBasketProducts$: Observable<Product[]>;

  constructor(private readonly userService: UserService, protected readonly location: Location,
              private readonly store: Store) {
  }

  ngOnInit(): void {
    this.loadBasketProducts$ = this.userService.loadBasketProducts();
  }

  removeProductFromBasket(identifier: string) {
    this.store.dispatch(removeFromBasket({identifier}));
    this.loadBasketProducts$ = this.store.select(selectBasketProductsState);
  }
}
