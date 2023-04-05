import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { ProductService } from "../../service/product.service";
import { deleteProductAction, deleteProductActionFail, deleteProductActionSuccess } from "../action/product.action";
import { catchError, exhaustMap, map, of, tap } from "rxjs";
import { Router } from "@angular/router";
import { NotifierService } from "angular-notifier";
import { NotificationType } from "../../enumeration/notification-enum";

@Injectable()
export class ProductEffects {
  constructor(private actions$: Actions, private productService: ProductService,
              private router: Router, private notifier: NotifierService) {
  }

  deleteProduct$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(deleteProductAction),
      exhaustMap((({ identifier }) => {
        return this.productService.deleteProduct(identifier)
          .pipe(
            map((httpResponse) => deleteProductActionSuccess({ httpResponse })),
            tap({
              next: (response) => {
                this.router.navigateByUrl('/admin/cockpit').then(() => {
                  this.notifier.notify(NotificationType.SUCCESS, response.httpResponse.message);
                });
              }
            }),
            catchError(error => of(deleteProductActionFail({ error })))
          );
      }))
    )
  });
}
