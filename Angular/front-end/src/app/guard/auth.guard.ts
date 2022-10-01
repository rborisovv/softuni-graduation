import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';

@Injectable()
export class AuthGuard implements CanActivate {
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot):
    Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    const name = "X-XSRF-JWT";
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    const jwtCookie = parts.pop().split(';').shift();

    return !!jwtCookie;
  }
}
