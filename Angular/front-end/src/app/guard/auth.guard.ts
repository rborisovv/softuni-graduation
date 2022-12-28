import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivateChild, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {CookieService} from "ngx-cookie-service";

@Injectable()
export class AuthGuard implements CanActivateChild {
  constructor(private cookieService: CookieService, private router: Router) {
  }

  canActivateChild(childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot):
    Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    const isAnonymous = !this.cookieService.check('JWT-TOKEN');

    if (!isAnonymous) {
      this.router.navigateByUrl('/home').then();
    }
    return isAnonymous;
  }
}
