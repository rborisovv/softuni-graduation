import { Injectable } from '@angular/core';
import { Router, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { JwtHelperService } from "@auth0/angular-jwt";
import { CookieService } from "ngx-cookie-service";

@Injectable({
  providedIn: 'root'
})
export class AuthGuard {
  constructor(private router: Router, private jwtService: JwtHelperService, private cookieService: CookieService) {

  }

  canActivate(): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const token = this.cookieService.get('JWT-TOKEN');
    const isExpired = this.jwtService.isTokenExpired(token);

    if (!isExpired) {
      return this.router.parseUrl('/home');
    }

    return isExpired;
  }
}
