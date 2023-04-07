import { Injectable } from '@angular/core';
import { Router, UrlTree } from '@angular/router';
import { catchError, first, Observable, of, tap } from 'rxjs';
import { UserService } from "../service/user.service";

@Injectable({
  providedIn: 'root'
})
export class AdminGuard {


  constructor(private userService: UserService, private router: Router) {
  }

  canActivateChild(): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.userService.isAdmin().pipe(
      catchError(() => of(this.router.parseUrl('/home'))),
      tap(response => response || this.router.parseUrl('/home')),
      first()
    );
  }
}
