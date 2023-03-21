import { Jwt } from "../authentication/Jwt";
import { CookieService } from "ngx-cookie-service";
import { UserService } from "./user.service";
import { Router } from "@angular/router";
import { Subscription, take } from "rxjs";

export function createFormData(data: any): FormData {
  const formData = new FormData();
  for (let key in data) {
    formData.set(key, data[key]);
  }
  return formData;
}

export function roleIsAdmin(): boolean {
  const jwtToken = Jwt.obtainJwtHeader();
  let decodedJwt = JSON.parse(window.atob(jwtToken.split('.')[1]));
  return decodedJwt.role === 'ADMIN';
}

export function logout(event: Event, cookieService: CookieService, userService: UserService,
                       router: Router): Subscription {
  event.preventDefault();
  return userService.logoutUser().pipe(take(1)).subscribe({
    next: () => {
      cookieService.delete('XSRF-TOKEN', "/", "localhost", false, "Lax");
      cookieService.delete('X-XSRF-JWT', "/", "localhost", false, "Lax");
      cookieService.delete('JWT-TOKEN', "/", "localhost", false, "Lax");
      router.navigateByUrl("/auth/login");
    }
  });
}
