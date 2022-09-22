import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {IUser} from "../interface/user-login";
import {environment} from "../../environments/environment.prod";
import {Observable} from "rxjs";

@Injectable()
export class UserService {
  private apiUrl: string = environment.apiHost;

  constructor(private http: HttpClient) {
  }

  public loginUser(user: IUser): Observable<IUser> {
    return this.http.post<IUser>(`${this.apiUrl}/user/login`, user, {withCredentials: true});
  }

  public generateFormData(user: IUser): FormData {
    const formData = new FormData();
    formData.set('username', user.username);
    // formData.set('email', user.email);
    // formData.set('firstName', user.firstName);
    // formData.set('lastName', user.lastName);
    formData.set('password', user.password);

    return formData;
  }
}
