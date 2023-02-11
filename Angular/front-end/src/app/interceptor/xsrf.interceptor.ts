import {Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";
import {Jwt} from "../authentication/Jwt";

@Injectable()
export class XsrfInterceptor implements HttpInterceptor {
  intercept(httpRequest: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const csrfToken: string = 'XSRF-TOKEN';

    return next.handle(httpRequest.clone({
      setHeaders: {
        'X-XSRF-TOKEN': XsrfInterceptor.obtainCsrfHeader(csrfToken),
        'JWT-TOKEN': Jwt.obtainJwtHeader()
      }
    }));
  }

  private static obtainCsrfHeader(name: string): any {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    return parts.pop().split(';').shift();
  }
}
