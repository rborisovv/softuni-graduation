import {Injectable} from "@angular/core";
import {Actions, createEffect, ofType} from "@ngrx/effects";
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
import {catchError, exhaustMap, map, of, tap} from "rxjs";
import {CategoryService} from "../../service/category.service";
import {Router} from "@angular/router";
import {NotifierService} from "angular-notifier";
import {NotificationType} from "../../enumeration/notification-enum";
import {Store} from "@ngrx/store";

@Injectable()
export class CategoryEffects {
  constructor(private readonly actions$: Actions, private readonly categoryService: CategoryService,
              private readonly router: Router, private readonly notifier: NotifierService) {
  }

  createCategory$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(createCategoryAction),
      exhaustMap(({formData}) => {
        return this.categoryService.createCategory(formData)
          .pipe(
            map(httpResponse => createCategoryActionSuccess({httpResponse: httpResponse})),
            tap((response) => {
              this.router.navigateByUrl('/admin/categories').then(() => {
                this.notifier.notify(NotificationType.SUCCESS, response.httpResponse.message);
              });
            }),
            catchError((error) => of(createCategoryActionFail({error: error})))
          );
      })
    );
  });

  updateCategory$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(updateCategoryAction),
      exhaustMap(({formData}) => {
        return this.categoryService.updateCategory(formData)
          .pipe(
            map(httpResponse => updateCategoryActionSuccess({httpResponse: httpResponse})),
            tap(response => {
              this.router.navigateByUrl('/admin/categories').then(() => {
                this.notifier.notify(NotificationType.SUCCESS, response.httpResponse.message);
              });
            }),
            catchError((error) => of(updateCategoryActionFail({error: error})))
          );
      })
    );
  });

  deleteCategory$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(deleteCategoryAction),
      exhaustMap(({identifier}) => {
        return this.categoryService.deleteCategory(identifier)
          .pipe(
            map(httpResponse => deleteCategoryActionSuccess({httpResponse: httpResponse})),
            tap((response) => {
              this.router.navigateByUrl('/admin/categories').then(() => {
                this.notifier.notify(NotificationType.SUCCESS, response.httpResponse.message);
              });
            })
          );
      }),
      catchError((error) => of(deleteCategoryActionFail({error: error})))
    );
  });
}
