import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import {
  createCategoryAction,
  createCategoryActionSuccess,
  createCategoryActionFail,
  updateCategoryAction,
  updateCategoryActionSuccess,
  updateCategoryActionFail,
  deleteCategoryAction,
  deleteCategoryActionFail,
  deleteCategoryActionSuccess
} from "../action/category.action";
import { catchError, exhaustMap, map, of, tap } from "rxjs";
import { CategoryService } from "../../service/category.service";
import { Router } from "@angular/router";
import { NotifierService } from "angular-notifier";
import { NotificationType } from "../../enumeration/notification-enum";

@Injectable()
export class CategoryEffects {
  constructor(private actions$: Actions, private categoryService: CategoryService,
              private router: Router, private notifier: NotifierService) {
  }

  createCategory$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(createCategoryAction),
      exhaustMap(({ formData }) => {
        return this.categoryService.createCategory(formData)
          .pipe(
            map(httpResponse => createCategoryActionSuccess({ httpResponse })),
            tap({
              next: (response) => {
                this.router.navigateByUrl('/admin/categories').then(() => {
                  this.notifier.notify(NotificationType.SUCCESS, response.httpResponse.message);
                });
              }
            }),
            catchError((error) => of(createCategoryActionFail({ error })))
          );
      })
    );
  });

  updateCategory$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(updateCategoryAction),
      exhaustMap(({ formData }) => {
        return this.categoryService.updateCategory(formData)
          .pipe(
            map(httpResponse => updateCategoryActionSuccess({ httpResponse })),
            tap({
              next: (response) => {
                this.router.navigateByUrl('/admin/categories').then(() => {
                  this.notifier.notify(NotificationType.SUCCESS, response.httpResponse.message);
                });
              }
            }),
            catchError((error) => of(updateCategoryActionFail({ error })))
          );
      })
    );
  });

  deleteCategory$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(deleteCategoryAction),
      exhaustMap(({ identifier }) => {
        return this.categoryService.deleteCategory(identifier)
          .pipe(
            map(httpResponse => deleteCategoryActionSuccess({ httpResponse })),
            tap({
                next: (response) => {
                  this.router.navigateByUrl('/admin/categories').then(() => {
                    this.notifier.notify(NotificationType.SUCCESS, response.httpResponse.message);
                  });
                }
              }
            )
          );
      }),
      catchError((error) => of(deleteCategoryActionFail({ error: error })))
    );
  });
}
