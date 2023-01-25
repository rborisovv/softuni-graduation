import {Injectable} from "@angular/core";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {MediaService} from "../../service/media.service";
import {
  createMediaAction,
  createMediaActionFail,
  createMediaActionSuccess,
  deleteMediaAction, deleteMediaActionFail, deleteMediaActionSuccess
} from "../action/media.action";
import {catchError, exhaustMap, map, of, tap} from "rxjs";
import {Router} from "@angular/router";
import {NotifierService} from "angular-notifier";
import {NotificationType} from "../../enumeration/notification-enum";

@Injectable()
export class MediaEffects {
  constructor(private readonly actions$: Actions, private readonly mediaService: MediaService,
              private readonly router: Router, private readonly notifier: NotifierService) {
  }

  createMedia$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(createMediaAction),
      exhaustMap(({formData}) => {
        return this.mediaService.createMedia(formData)
          .pipe(
            map(response => createMediaActionSuccess({httpResponse: response})),
            tap((response) => {
              this.router.navigateByUrl('/admin/medias').then(() => {
                this.notifier.notify(NotificationType.SUCCESS, response.httpResponse.message);
              });
            })
          );
      }), catchError(error => of(createMediaActionFail({error: error})))
    );
  });

  deleteMedia$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(deleteMediaAction),
      exhaustMap(({pk}) => {
        return this.mediaService.deleteMedia(pk)
          .pipe(
            map(response => deleteMediaActionSuccess({httpResponse: response})),
            tap((response) => {
              this.router.navigateByUrl('/admin/medias').then(() => {
                this.notifier.notify(NotificationType.SUCCESS, response.httpResponse.message);
              });
            }),
            catchError(error => of(deleteMediaActionFail({error: error})))
          );
      })
    )
  })
}
