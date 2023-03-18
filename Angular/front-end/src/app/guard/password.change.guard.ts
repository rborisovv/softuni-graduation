import { Injectable } from '@angular/core';
import {
  ActivatedRoute,
  ActivatedRouteSnapshot,
  CanActivate,
  Router,
  RouterStateSnapshot,
  UrlTree
} from '@angular/router';
import { Observable, take, tap } from 'rxjs';
import { UserService } from "../service/user.service";
import { NotifierService } from "angular-notifier";
import { NotificationType } from "../enumeration/notification-enum";

@Injectable({
  providedIn: 'root'
})
export class PasswordChangeGuard implements CanActivate {
  constructor(private userService: UserService,
              private router: Router,
              private notifier: NotifierService) {
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    const token = route.queryParams['token'];
    return this.userService.hasActivePasswordRequest(token).pipe(
      take(1), tap(response => {
        if (response === false) {
          this.router.navigateByUrl('/home').then(() => {
            this.notifier.notify(NotificationType.INFO, 'The provided token is invalid!');
          });
        }
      })
    );
  }
}
