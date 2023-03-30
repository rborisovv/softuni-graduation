import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { UserService } from "../../service/user.service";
import {
  createVoucher, createVoucherFail, createVoucherSuccess,
  loginAction,
  loginActionFail,
  loginActionSuccess,
  registerAction, registerActionFail,
  registerActionSuccess
} from "../action/auth.action";
import { catchError, exhaustMap, map, of, tap } from "rxjs";
import { Router } from "@angular/router";
import { NotifierService } from "angular-notifier";
import { NotificationType } from "../../enumeration/notification-enum";
import { LOGIN_SUCCESS } from "../../common/messages";

@Injectable()
export class AuthEffects {
  constructor(private readonly actions$: Actions, private readonly userService: UserService,
              private readonly router: Router, private readonly notifier: NotifierService) {
  }

  loginUser$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(loginAction),
      exhaustMap(({ formData }) => {
        return this.userService.loginUser(formData)
          .pipe(
            map(user => loginActionSuccess({ username: user.username, email: user.email })),
            tap((user) => {
              this.router.navigateByUrl('/home').then(() => {
                this.notifier.notify(NotificationType.SUCCESS, LOGIN_SUCCESS + user.username);
              });
            }),
            catchError((error) => of(loginActionFail({ error })))
          );
      })
    );
  });

  registerUser$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(registerAction),
      exhaustMap(({ registerModel }) => {
          return this.userService.registerUser(registerModel)
            .pipe(
              map(user => registerActionSuccess({ username: user.username })),
              tap((user) => {
                this.router.navigateByUrl('/auth/login').then(() => {
                  this.notifier.notify(NotificationType.SUCCESS,
                    `User with username "${user.username}" successfully registered!`);
                });
              }),
              catchError((error) => of(registerActionFail({ error })))
            )
        }
      )
    )
  });

  createVoucher$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(createVoucher),
      exhaustMap(({ voucher }) => {
          return this.userService.createVoucher(voucher)
            .pipe(
              map(response => createVoucherSuccess({ response })),
              tap((response) => {
                this.router.navigateByUrl('/admin/vouchers').then(() => {
                  this.notifier.notify(NotificationType.SUCCESS, response.response.message);
                });
              }),
              catchError((error) => of(createVoucherFail({ error })))
            )
        }
      )
    )
  });
}
