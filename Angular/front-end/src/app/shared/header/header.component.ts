import {
  Component,
  ElementRef,
  OnDestroy,
  Renderer2,
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

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  // changeDetection: ChangeDetectionStrategy.OnPush
})

export class HeaderComponent implements OnDestroy {

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

  @ViewChild('products') products: ElementRef;

  constructor(private userService: UserService,
              private cookieService: CookieService,
              private router: Router,
              private readonly store: Store,
              private readonly renderer: Renderer2) {
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

  fetchFavouriteProducts(): void {
    this.favouriteProducts$ = this.userService.loadFavouriteProducts();
  }

  removeProductFromFavourites(productRef: HTMLDivElement, identifier: string): void {
    this.store.dispatch(removeFromFavourites({identifier}));
    this.renderer.removeChild(this.products.nativeElement, productRef);
  }
}
