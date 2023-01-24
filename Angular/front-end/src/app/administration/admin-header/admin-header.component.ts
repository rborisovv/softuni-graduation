import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {faCartShopping, faHome, faSignOut} from '@fortawesome/free-solid-svg-icons';
import {CookieService} from "ngx-cookie-service";
import {UserService} from "../../service/user.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-admin-header',
  templateUrl: './admin-header.component.html',
  styleUrls: ['./admin-header.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AdminHeaderComponent implements OnInit {

  faCart = faCartShopping;

  faSignOut = faSignOut;

  faHome = faHome

  constructor(private cookieService: CookieService, private userService: UserService, private router: Router) {
  }

  ngOnInit(): void {
  }


  logout(): void {
    this.userService.logoutUser().subscribe({
      next: () => {
        this.cookieService.delete('JWT-TOKEN', '/', 'localhost');
      },
      complete: () => {
        this.router.navigateByUrl('/auth/login');
      }
    });
  }
}
