import {Actions, createEffect, ofType} from "@ngrx/effects";
import {NotifierService} from "angular-notifier";
import {UserService} from "../../service/user.service";
import {
  addToBasket,
  addToBasketFail,
  addToBasketSuccess,
  addToFavourites,
  addToFavouritesFail,
  addToFavouritesSuccess,
  fetchRenewedBasketProducts,
  fetchRenewedFavouriteProducts,
  removeFromBasket,
  removeFromBasketFail, removeFromBasketSuccess,
  removeFromFavourites,
  removeFromFavouritesFail,
  removeFromFavouritesSuccess
} from "../action/user.action";
import {catchError, exhaustMap, map, of, tap} from "rxjs";
import {Injectable} from "@angular/core";
import {Store} from "@ngrx/store";

@Injectable()
export class UserEffects {
  constructor(private readonly actions$: Actions, private userService: UserService,
              private readonly notifier: NotifierService, private store: Store) {
  }

  addToFavourites$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(addToFavourites),
      exhaustMap((({identifier}) => {
        return this.userService.addToFavourites(identifier)
          .pipe(
            map(response => addToFavouritesSuccess({httpResponse: response})),
            tap((response) => {
              this.notifier.notify(response.httpResponse.notificationStatus, response.httpResponse.message);
              this.store.dispatch(fetchRenewedFavouriteProducts({httpResponse: response.httpResponse}));
            }),
            catchError(error => of(addToFavouritesFail({error: error})))
          );
      }))
    )
  });

  removeFromFavourites$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(removeFromFavourites),
      exhaustMap((({identifier}) => {
        return this.userService.removeFromFavourites(identifier)
          .pipe(
            map(response => removeFromFavouritesSuccess({httpResponse: response})),
            tap((response) => {
              this.notifier.notify(response.httpResponse.notificationStatus, response.httpResponse.message);
              this.store.dispatch(fetchRenewedFavouriteProducts({httpResponse: response.httpResponse}));
            }),
            catchError(error => of(removeFromFavouritesFail({error: error})))
          );
      }))
    )
  });

  addToBasket$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(addToBasket),
      exhaustMap((({identifier}) => {
        return this.userService.addToBasket(identifier)
          .pipe(
            map(response => addToBasketSuccess({httpResponse: response})),
            tap((response) => {
              this.notifier.notify(response.httpResponse.notificationStatus, response.httpResponse.message);
              this.store.dispatch(fetchRenewedBasketProducts({httpResponse: response.httpResponse}));
            }),
            catchError(error => of(addToBasketFail({error: error})))
          );
      }))
    )
  });

  removeFromBasket$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(removeFromBasket),
      exhaustMap((({identifier}) => {
        return this.userService.removeFromBasket(identifier)
          .pipe(
            map(response => removeFromBasketSuccess({httpResponse: response})),
            tap((response) => {
              this.notifier.notify(response.httpResponse.notificationStatus, response.httpResponse.message);
              this.store.dispatch(fetchRenewedBasketProducts({httpResponse: response.httpResponse}));
            }),
            catchError(error => of(removeFromBasketFail({error: error})))
          );
      }))
    )
  });
}
