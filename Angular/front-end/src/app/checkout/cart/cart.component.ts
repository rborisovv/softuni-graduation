import {Component, OnInit} from '@angular/core';
import {UserService} from "../../service/user.service";
import {Observable} from "rxjs";
import {Product} from "../../interface/product";
import {Location} from "@angular/common";

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {
  loadBasketProducts$: Observable<Product[]>;

  constructor(private readonly userService: UserService, protected readonly location: Location) {
  }

  ngOnInit(): void {
    this.loadBasketProducts$ = this.userService.loadBasketProducts();
  }

  removeProductFromBasket(identifier: string) {

  }
}
