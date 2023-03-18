import { Actions, createEffect, ofType } from "@ngrx/effects";
import { NotifierService } from "angular-notifier";
import { UserService } from "../../service/user.service";
import {
  addToBasket,
  addToBasketFail,
  addToBasketSuccess,
  addToFavourites,
  addToFavouritesFail,
  addToFavouritesSuccess, changePassword, changePasswordFail, changePasswordSuccess,
  createOrder,
  createOrderFail,
  createOrderSuccess,
  fetchRenewedBasketProducts,
  fetchRenewedFavouriteProducts,
  removeFromBasket,
  removeFromBasketFail,
  removeFromBasketSuccess,
  removeFromFavourites,
  removeFromFavouritesFail,
  removeFromFavouritesSuccess,
  resetPassword, resetPasswordFail,
  resetPasswordSuccess,
  submitCheckoutFlow,
  submitCheckoutFlowFail,
  submitCheckoutFlowSuccess,
  updateBasketProductQuantity,
  updateBasketProductQuantityFail,
  updateBasketProductQuantitySuccess
} from "../action/user.action";
import { catchError, exhaustMap, map, mergeMap, of, tap } from "rxjs";
import { Injectable } from "@angular/core";
import { Store } from "@ngrx/store";
import { Router } from "@angular/router";
import { NotificationType } from "../../enumeration/notification-enum";
import { ORDER_CREATED } from "../../common/messages";

@Injectable()
export class UserEffects {
  constructor(private readonly actions$: Actions, private userService: UserService,
              private readonly notifier: NotifierService, private store: Store,
              private readonly router: Router) {
  }

  addToFavourites$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(addToFavourites),
      exhaustMap((({ identifier }) => {
        return this.userService.addToFavourites(identifier)
          .pipe(
            map(response => addToFavouritesSuccess({ httpResponse: response })),
            tap((response) => {
              this.notifier.notify(response.httpResponse.notificationStatus, response.httpResponse.message);
              this.store.dispatch(fetchRenewedFavouriteProducts({ httpResponse: response.httpResponse }));
            }),
            catchError(error => of(addToFavouritesFail({ error: error })))
          );
      }))
    )
  });

  removeFromFavourites$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(removeFromFavourites),
      exhaustMap((({ identifier }) => {
        return this.userService.removeFromFavourites(identifier)
          .pipe(
            map(response => removeFromFavouritesSuccess({ httpResponse: response })),
            tap((response) => {
              this.notifier.notify(response.httpResponse.notificationStatus, response.httpResponse.message);
              this.store.dispatch(fetchRenewedFavouriteProducts({ httpResponse: response.httpResponse }));
            }),
            catchError(error => of(removeFromFavouritesFail({ error: error })))
          );
      }))
    )
  });

  addToBasket$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(addToBasket),
      exhaustMap((({ identifier }) => {
        return this.userService.addToBasket(identifier)
          .pipe(
            map(response => addToBasketSuccess({ httpResponse: response })),
            tap((response) => {
              this.notifier.notify(response.httpResponse.notificationStatus, response.httpResponse.message);
              this.store.dispatch(fetchRenewedBasketProducts({ httpResponse: response.httpResponse }));
            }),
            catchError(error => of(addToBasketFail({ error: error })))
          );
      }))
    )
  });

  removeFromBasket$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(removeFromBasket),
      exhaustMap((({ identifier }) => {
        return this.userService.removeFromBasket(identifier)
          .pipe(
            map(response => removeFromBasketSuccess({ httpResponse: response })),
            tap((response) => {
              this.notifier.notify(response.httpResponse.notificationStatus, response.httpResponse.message);
              this.store.dispatch(fetchRenewedBasketProducts({ httpResponse: response.httpResponse }));
            }),
            catchError(error => of(removeFromBasketFail({ error: error })))
          );
      }))
    )
  });

  updateBasketProductQuantity = createEffect(() => {
    return this.actions$.pipe(
      ofType(updateBasketProductQuantity),
      exhaustMap((({ identifier, quantity }) => {
        return this.userService.updateBasketProductQuantity(identifier, quantity)
          .pipe(
            map(response => updateBasketProductQuantitySuccess({ httpResponse: response })),
            tap((response) => {
              this.store.dispatch(fetchRenewedBasketProducts({ httpResponse: response.httpResponse }));
            }),
            catchError(error => of(updateBasketProductQuantityFail({ error: error })))
          );
      }))
    )
  });

  submitCheckoutFlow = createEffect(() => {
    return this.actions$.pipe(
      ofType(submitCheckoutFlow),
      mergeMap((({ checkout }) => {
        return this.userService.submitCheckoutFlow(checkout)
          .pipe(
            map(() => submitCheckoutFlowSuccess),
            tap(() => {
              this.router.navigateByUrl('/finalize-order');
            }),
            catchError(error => of(submitCheckoutFlowFail({ error: error })))
          );
      }))
    )
  });

  createOrder = createEffect(() => {
    return this.actions$.pipe(
      ofType(createOrder),
      exhaustMap(() => {
        return this.userService.createOrder()
          .pipe(
            map(() => createOrderSuccess()),
            tap(() => {
              this.router.navigateByUrl('/order-created').then(() => {
                this.notifier.notify(NotificationType.SUCCESS, ORDER_CREATED);
              });
            }),
            catchError(error => of(createOrderFail({ error: error })))
          );
      }));
  });

  resetPassword = createEffect(() => {
    return this.actions$.pipe(
      ofType(resetPassword),
      exhaustMap(({ email }) => {
        return this.userService.resetPassword(email)
          .pipe(
            map((response) => resetPasswordSuccess({ httpResponse: response })),
            tap((response) => {
              this.notifier.notify(response.httpResponse.notificationStatus, response.httpResponse.message);
            }),
            catchError(error => of(resetPasswordFail({ error: error })))
          );
      }));
  });

  changePassword = createEffect(() => {
    return this.actions$.pipe(
      ofType(changePassword),
      exhaustMap(({ data }) => {
        return this.userService.changePassword(data)
          .pipe(
            map((response) => changePasswordSuccess({ response: response })),
            tap((httpResponse) => {
              this.router.navigateByUrl('/auth/login').then(() => {
                this.notifier.notify(httpResponse.response.notificationStatus, httpResponse.response.message);
              });
            }),
            catchError(error => of(changePasswordFail({ error: error })))
          );
      }));
  });
}
