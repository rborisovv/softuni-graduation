import {Component, OnInit} from '@angular/core';
import {faAngleDown, faCartShopping} from '@fortawesome/free-solid-svg-icons';
import {UserService} from "../../service/user.service";
import {CookieService} from "ngx-cookie-service";
import {Router} from "@angular/router";

declare function loadNavbar(): any;

declare function handleSmallScreens(): any;

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})

export class HeaderComponent implements OnInit {
  faAngleDown = faAngleDown;

  faCart = faCartShopping;

  constructor(private userService: UserService,
              private cookieService: CookieService,
              private router: Router) {
  }

  ngOnInit(): void {
    loadNavbar();
    handleSmallScreens();
  }

  onLogout(event: Event): void {
    event.preventDefault();
    this.userService.logoutUser().subscribe({
      next: () => {
        this.cookieService.delete('XSRF-TOKEN', "/", "localhost", false, "Lax");
        this.cookieService.delete('X-XSRF-JWT', "/", "localhost", false, "Lax");
        this.router.navigateByUrl("/login");
      }
    })
  }
}
