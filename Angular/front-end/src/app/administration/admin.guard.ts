import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivateChild, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {catchError, first, Observable, of, tap} from 'rxjs';
import {UserService} from "../service/user.service";

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivateChild {


  constructor(private userService: UserService, private router: Router) {
  }

  canActivateChild(
    childRoute: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.userService.isAdmin().pipe(
      catchError(() => of(this.router.parseUrl('/home'))),
      tap(response => response || this.router.parseUrl('/home')),
      first()
    );
  }
}
