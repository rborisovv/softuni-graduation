import {Component, OnInit} from '@angular/core';
import {faAngleDown} from '@fortawesome/free-solid-svg-icons';
import {UserService} from "../../service/user.service";
import {CookieService} from "ngx-cookie-service";
import {NotifierService} from "angular-notifier";
import {NotificationType} from "../../enumeration/notification-enum";
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

  constructor(private userService: UserService,
              private cookieService: CookieService,
              private notifier: NotifierService,
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
        this.notifier.notify(NotificationType.SUCCESS, `Successfully logged out of the application`);
      }
    })
  }
}
