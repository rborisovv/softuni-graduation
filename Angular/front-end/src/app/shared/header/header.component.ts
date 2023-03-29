import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  Input,
  OnChanges,
  OnDestroy,
  OnInit,
  SimpleChanges,
  ViewChild
} from '@angular/core';
import {
  faCartShopping,
  faHeart,
  faMagnifyingGlass,
  faShoppingBasket,
  faSignOut,
  faTrash,
  faUserAlt,
  faUserSecret
} from '@fortawesome/free-solid-svg-icons';
import { UserService } from "../../service/user.service";
import { CookieService } from "ngx-cookie-service";
import { Router } from "@angular/router";
import { Observable, Subscription } from "rxjs";
import { Product } from "../../model/product";
import { Store } from "@ngrx/store";
import { addToBasket, removeFromFavourites } from "../../store/action/user.action";
import { selectBasketProductsState, selectFavouriteProductsState } from "../../store/selector/user.selector";
import { logout, roleIsAdmin } from "../../service/service.index";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})

export class HeaderComponent implements OnInit, OnDestroy, OnChanges {

  faCart = faCartShopping;
  faBasket = faShoppingBasket;
  faHeart = faHeart;
  faGlass = faMagnifyingGlass;
  faUserSecret = faUserSecret;
  faSignOut = faSignOut;
  faUserAlt = faUserAlt;
  faTrash = faTrash;

  subscriptions: Subscription[] = [];
  favouriteProducts$: Observable<Product[]>;
  basketProducts$: Observable<Product[]>;

  @Input() renewedFavouriteProducts$: Observable<Product[]>;
  @Input() renewedBasketProducts$: Observable<Product[]>;

  @ViewChild('products') products: ElementRef;

  constructor(private userService: UserService, private cookieService: CookieService,
              private router: Router, private store: Store) {
  }

  ngOnInit(): void {
    this.favouriteProducts$ = this.userService.loadFavouriteProducts();
    this.basketProducts$ = this.userService.loadBasketProducts();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['renewedFavouriteProducts$'] && changes['renewedFavouriteProducts$'].previousValue !== changes['renewedFavouriteProducts$'].currentValue) {
      this.favouriteProducts$ = changes['renewedFavouriteProducts$'].currentValue;
    }
    if (changes['renewedBasketProducts$'] && changes['renewedBasketProducts$'].previousValue !== changes['renewedBasketProducts$'].currentValue) {
      this.basketProducts$ = changes['renewedBasketProducts$'].currentValue;
    }

    //TODO: Find why only one counter is display upon clicking one of the items
  }

  onLogout(event: Event): void {
    logout(event, this.cookieService, this.userService, this.router);
  }

  roleIsAdmin(): boolean {
    return roleIsAdmin();
  }

  removeProductFromFavourites(productRef: HTMLDivElement, identifier: string, event: MouseEvent): void {
    event.preventDefault();
    this.store.dispatch(removeFromFavourites({ identifier }));
    this.favouriteProducts$ = this.store.select(selectFavouriteProductsState);
  }

  addProductToBasket(identifier: string) {
    this.store.dispatch(addToBasket({ identifier }));
    this.basketProducts$ = this.store.select(selectBasketProductsState);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(x => x.unsubscribe());
  }
}
