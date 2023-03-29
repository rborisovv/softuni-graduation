import { Component, ElementRef, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { logout, roleIsAdmin } from "../../service/service.index";
import { UserService } from "../../service/user.service";
import { CookieService } from "ngx-cookie-service";
import { Router } from "@angular/router";
import {
  faCartShopping,
  faSignOut,
  faUserAlt,
  faUserSecret
} from "@fortawesome/free-solid-svg-icons";
import { Observable } from "rxjs";
import { Product } from "../../model/product";

@Component({
  selector: 'app-checkout-header',
  templateUrl: './checkout-header.component.html',
  styleUrls: ['./checkout-header.component.scss']
})
export class CheckoutHeaderComponent implements OnInit, OnDestroy {
  constructor(private userService: UserService,
              private cookieService: CookieService,
              private router: Router) {
  }

  faCart = faCartShopping;
  faUserSecret = faUserSecret;
  faSignOut = faSignOut;
  faUserAlt = faUserAlt;

  favouriteProducts$: Observable<Product[]>;
  basketProducts$: Observable<Product[]>;

  @Input() renewedFavouriteProducts$: Observable<Product[]>;
  @Input() renewedBasketProducts$: Observable<Product[]>;

  @ViewChild('products') products: ElementRef;

  ngOnDestroy(): void {

  }

  ngOnInit(): void {
    this.favouriteProducts$ = this.userService.loadFavouriteProducts();
    this.basketProducts$ = this.userService.loadBasketProducts();
  }

  onLogout(event: Event): void {
    logout(event, this.cookieService, this.userService, this.router);
  }

  roleIsAdmin(): boolean {
    return roleIsAdmin();
  }
}
