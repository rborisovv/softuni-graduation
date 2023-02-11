import {Component, OnDestroy} from '@angular/core';
import {faCartShopping, faHeart, faMagnifyingGlass, faShoppingBasket, faSignOut, faUserSecret} from '@fortawesome/free-solid-svg-icons';
import {UserService} from "../../service/user.service";
import {CookieService} from "ngx-cookie-service";
import {Router} from "@angular/router";
import {Subscription} from "rxjs";
import {Jwt} from "../../authentication/Jwt";
import {JwtHelperService} from "@auth0/angular-jwt";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})

export class HeaderComponent implements OnDestroy {

  faCart = faCartShopping;
  faBasket = faShoppingBasket;
  faHeart = faHeart;
  faGlass = faMagnifyingGlass;
  faUserSecret = faUserSecret;
  faSignOut = faSignOut;

  const
  subscriptions: Subscription[] = [];

  constructor(private userService: UserService,
              private cookieService: CookieService,
              private router: Router) {
  }

  onLogout(event: Event): void {
    event.preventDefault();
    const subscription = this.userService.logoutUser().subscribe({
      next: () => {
        this.cookieService.delete('XSRF-TOKEN', "/", "localhost", false, "Lax");
        this.cookieService.delete('X-XSRF-JWT', "/", "localhost", false, "Lax");
        this.cookieService.delete('JWT-TOKEN', "/", "localhost", false, "Lax");
        this.router.navigateByUrl("/login");
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
}
