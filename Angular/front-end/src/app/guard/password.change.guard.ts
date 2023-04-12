import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Router,
  UrlTree
} from '@angular/router';
import { Observable, tap } from 'rxjs';
import { UserService } from "../service/user.service";
import { NotifierService } from "angular-notifier";
import { NotificationType } from "../enumeration/notification-enum";
import { TOKEN_IS_INVALID } from "../common/messages";

@Injectable({
  providedIn: 'root'
})
export class PasswordChangeGuard {
  constructor(
    private userService: UserService,
    private router: Router,
    private notifier: NotifierService
  ) {
  }

  canActivate(route: ActivatedRouteSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    const token = route.queryParams['token'];
    return this.userService.hasActivePasswordRequest(token).pipe(tap(response => {
        if (response === false) {
          this.router.navigateByUrl('/home').then(() => {
            this.notifier.notify(NotificationType.INFO, TOKEN_IS_INVALID);
          });
        }
      })
    );
  }
}
