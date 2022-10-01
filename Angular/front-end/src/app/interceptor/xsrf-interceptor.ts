import {Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable()
export class XsrfInterceptor implements HttpInterceptor {
  intercept(httpRequest: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const csrfToken: string = 'XSRF-TOKEN';
    const jwtToken: string = 'X-XSRF-JWT';


    return next.handle(httpRequest.clone({
      setHeaders: {
        'X-XSRF-TOKEN': XsrfInterceptor.obtainCsrfHeader(csrfToken),
        'X-XSRF-JWT': XsrfInterceptor.obtainJwtHeader(jwtToken)
      }
    }));
  }

  private static obtainCsrfHeader(name: string): any {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    return parts.pop().split(';').shift();
  }

  private static obtainJwtHeader(name: string): any {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    return 'Bearer ' + parts.pop().split(';').shift();
  }
}
