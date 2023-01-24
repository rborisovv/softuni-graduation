import {Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {catchError, Observable} from "rxjs";
import {NotifierService} from "angular-notifier";
import {NotificationType} from "../enumeration/notification-enum";

@Injectable()
export class ErrorHandlerInterceptor implements HttpInterceptor {
  constructor(private notifier: NotifierService) {
  }

  intercept(err: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(err)
      .pipe(
        catchError((err) => {
          if (err instanceof Error) {
            //Client-side error occurred. Implement a handler logic.
          } else {
            //Backend error
            this.notifier.notify(NotificationType.ERROR, err.error.message);
          }
          throw err;
        })
      );
  }
}
