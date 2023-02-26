import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  Input,
  OnChanges,
  OnDestroy,
  OnInit,
  Renderer2,
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
import {UserService} from "../../service/user.service";
import {CookieService} from "ngx-cookie-service";
import {Router} from "@angular/router";
import {Observable, Subscription} from "rxjs";
import {Jwt} from "../../authentication/Jwt";
import {Product} from "../../interface/product";
import {Store} from "@ngrx/store";
import {removeFromFavourites} from "../../store/action/user.action";
import {selectFavouriteProductsState} from "../../store/selector/user.selector";

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

  @Input() renewedFavouriteProducts: Observable<Product[]>;
  @Input() renewedBasketProducts: Observable<Product[]>;

  @ViewChild('products') products: ElementRef;

  constructor(private userService: UserService,
              private cookieService: CookieService,
              private router: Router,
              private readonly store: Store,
              private readonly renderer: Renderer2) {
  }

  ngOnInit(): void {
    this.favouriteProducts$ = this.userService.loadFavouriteProducts();
    this.basketProducts$ = this.userService.loadBasketProducts();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['renewedFavouriteProducts'] && changes['renewedFavouriteProducts'].previousValue !== changes['renewedFavouriteProducts'].currentValue) {
      this.favouriteProducts$ = changes['renewedFavouriteProducts'].currentValue;
      // this.basketProducts$ = this.store.select(selectBasketProductsState);
      // this.favouriteProducts$.subscribe((x) => console.log('favourites' + x));
      // this.basketProducts$.subscribe((x) => console.log('basket' + x));
    }
    if (changes['renewedBasketProducts'] && changes['renewedBasketProducts'].previousValue !== changes['renewedBasketProducts'].currentValue) {
      this.basketProducts$ = changes['renewedBasketProducts'].currentValue;
      // this.favouriteProducts$ = this.store.select(selectFavouriteProductsState);
      // this.favouriteProducts$.subscribe((x) => console.log('favourites' + x));
      // this.basketProducts$.subscribe((x) => console.log('basket' + x));
    }

    //TODO: Find why only one counter is display upon clicking one of the items
  }

  onLogout(event: Event): void {
    event.preventDefault();
    const subscription = this.userService.logoutUser().subscribe({
      next: () => {
        this.cookieService.delete('XSRF-TOKEN', "/", "localhost", false, "Lax");
        this.cookieService.delete('X-XSRF-JWT', "/", "localhost", false, "Lax");
        this.cookieService.delete('JWT-TOKEN', "/", "localhost", false, "Lax");
        this.router.navigateByUrl("/auth/login");
      }
    });

    this.subscriptions.push(subscription);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(x => x.unsubscribe());
  }

  roleIsAdmin(): boolean {
    const jwtToken = Jwt.obtainJwtHeader();
    let decodedJwt = JSON.parse(window.atob(jwtToken.split('.')[1]));
    return decodedJwt.role === 'ADMIN';
  }

  removeProductFromFavourites(productRef: HTMLDivElement, identifier: string): void {
    this.store.dispatch(removeFromFavourites({identifier}));
    this.renderer.removeChild(this.products.nativeElement, productRef);
    this.favouriteProducts$ = this.store.select(selectFavouriteProductsState);
  }
}
