import { Component, OnInit } from '@angular/core';
import { Observable, take } from "rxjs";
import { Product } from "../../interface/product";
import { Router } from "@angular/router";
import { Store } from "@ngrx/store";
import { Location } from "@angular/common";
import { UserService } from "../../service/user.service";
import { Checkout } from "../../interface/checkout";

@Component({
  selector: 'app-finalize',
  templateUrl: './finalize.component.html',
  styleUrls: ['./finalize.component.scss']
})
export class FinalizeComponent implements OnInit {
  checkoutData: Checkout;

  constructor(private readonly store: Store, protected readonly router: Router, protected readonly location: Location,
              private readonly userService: UserService) {
  }

  renewedBasketProducts$: Observable<Product[]>;

  ngOnInit(): void {
    this.renewedBasketProducts$ = this.userService.loadBasketProducts();
    this.userService.fetchCheckoutDataIfPresent().pipe(take(1))
      .subscribe(data => this.checkoutData = data);
  }
}
