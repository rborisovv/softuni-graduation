import {Injectable} from "@angular/core";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {ProductService} from "../../service/product.service";
import {deleteProductAction, deleteProductActionFail, deleteProductActionSuccess} from "../action/product.action";
import {catchError, exhaustMap, map, of, tap} from "rxjs";
import {Router} from "@angular/router";
import {NotifierService} from "angular-notifier";
import {NotificationType} from "../../enumeration/notification-enum";

@Injectable()
export class ProductEffects {
  constructor(private readonly actions$: Actions, private readonly productService: ProductService,
              private readonly router: Router, private readonly notifier: NotifierService) {
  }

  deleteProduct$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(deleteProductAction),
      exhaustMap((({identifier}) => {
        return this.productService.deleteProduct(identifier)
          .pipe(
            map(response => deleteProductActionSuccess({httpResponse: response})),
            tap((response) => {
              this.router.navigateByUrl('/admin/cockpit').then(() => {
                this.notifier.notify(NotificationType.SUCCESS, response.httpResponse.message);
              })
            }),
            catchError(error => of(deleteProductActionFail({error: error})))
          );
      }))
    )
  });
}
