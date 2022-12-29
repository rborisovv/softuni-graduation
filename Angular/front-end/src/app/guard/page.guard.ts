import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {CookieService} from "ngx-cookie-service";

@Injectable({
  providedIn: 'root'
})
export class PageGuard implements CanActivate {
  constructor(private cookieService: CookieService, private router: Router) {
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    const token = this.cookieService.check('JWT-TOKEN');

    if (!token) {
      this.router.navigateByUrl('/auth/login');
    }

    return token;
  }
}
