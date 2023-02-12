import {Actions, createEffect, ofType} from "@ngrx/effects";
import {NotifierService} from "angular-notifier";
import {UserService} from "../../service/user.service";
import {addToFavourites, addToFavouritesFail, addToFavouritesSuccess} from "../action/user.action";
import {catchError, exhaustMap, map, of, tap} from "rxjs";
import {Injectable} from "@angular/core";

@Injectable()
export class UserEffects {
  constructor(private readonly actions$: Actions, private userService: UserService,
              private readonly notifier: NotifierService) {
  }

  addToFavourites$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(addToFavourites),
      exhaustMap((({identifier}) => {
        return this.userService.addToFavourites(identifier)
          .pipe(
            map(response => addToFavouritesSuccess({httpResponse: response})),
            tap((response) => {
              //todo: Add to number of favourite products
              console.log(response.httpResponse.notificationStatus)
              this.notifier.notify(response.httpResponse.notificationStatus, response.httpResponse.message);
            }),
            catchError(error => of(addToFavouritesFail({error: error})))
          );
      }))
    )
  });
}
