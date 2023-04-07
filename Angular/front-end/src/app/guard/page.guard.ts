import { Injectable } from '@angular/core';
import { Router, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { CookieService } from "ngx-cookie-service";
import { JwtHelperService } from "@auth0/angular-jwt";

@Injectable({
  providedIn: 'root'
})
export class PageGuard {

  constructor(private cookieService: CookieService, private router: Router, private jwtService: JwtHelperService) {
  }

  canActivate(): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    const token = this.cookieService.get('JWT-TOKEN');
    const isExpired = this.jwtService.isTokenExpired(token);

    if (isExpired) {
      return this.router.parseUrl('/auth/login');
    }

    return !isExpired;
  }
}
