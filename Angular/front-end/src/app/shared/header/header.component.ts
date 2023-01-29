import {Component, OnDestroy} from '@angular/core';
import {faCartShopping} from '@fortawesome/free-solid-svg-icons';
import {UserService} from "../../service/user.service";
import {CookieService} from "ngx-cookie-service";
import {Router} from "@angular/router";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})

export class HeaderComponent implements OnDestroy {

  faCart = faCartShopping;

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
        this.router.navigateByUrl("/login");
      }
    });

    this.subscriptions.push(subscription);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(x => x.unsubscribe());
  }
}
