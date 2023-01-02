import {Component, OnInit} from '@angular/core';
import {faAngleDown, faCartShopping, faSignOut} from '@fortawesome/free-solid-svg-icons';
import {CookieService} from "ngx-cookie-service";
import {UserService} from "../../service/user.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-admin-header',
  templateUrl: './admin-header.component.html',
  styleUrls: ['./admin-header.component.scss']
})
export class AdminHeaderComponent implements OnInit {

  faAngleDown = faAngleDown;

  faCart = faCartShopping;

  faSignOut = faSignOut;

  constructor(private cookieService: CookieService, private userService: UserService,
              private router: Router) {
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
