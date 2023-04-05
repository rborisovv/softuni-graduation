import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { MediaService } from "../../service/media.service";
import {
  createMediaAction,
  createMediaActionFail,
  createMediaActionSuccess,
  deleteMediaAction, deleteMediaActionFail, deleteMediaActionSuccess
} from "../action/media.action";
import { catchError, exhaustMap, map, of, tap } from "rxjs";
import { Router } from "@angular/router";
import { NotifierService } from "angular-notifier";
import { NotificationType } from "../../enumeration/notification-enum";

@Injectable()
export class MediaEffects {
  constructor(private actions$: Actions, private mediaService: MediaService,
              private router: Router, private notifier: NotifierService) {
  }

  createMedia$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(createMediaAction),
      exhaustMap(({ formData }) => {
        return this.mediaService.createMedia(formData)
          .pipe(
            map(httpResponse => createMediaActionSuccess({ httpResponse })),
            tap({
              next: (response) => {
                this.router.navigateByUrl('/admin/medias').then(() => {
                  this.notifier.notify(NotificationType.SUCCESS, response.httpResponse.message);
                });
              }
            })
          );
      }), catchError(error => of(createMediaActionFail({ error })))
    );
  });

  deleteMedia$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(deleteMediaAction),
      exhaustMap(({ pk }) => {
        return this.mediaService.deleteMedia(pk)
          .pipe(
            map(httpResponse => deleteMediaActionSuccess({ httpResponse })),
            tap({
              next: (response) => {
                this.router.navigateByUrl('/admin/medias').then(() => {
                  this.notifier.notify(NotificationType.SUCCESS, response.httpResponse.message);
                });
              }
            }),
            catchError(error => of(deleteMediaActionFail({ error })))
          );
      })
    )
  })
}
