import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment.prod";
import {Observable} from "rxjs";
import {UserWelcome} from "../interface/user.welcome";
import {UserRegisterModel} from "../authentication/register/userRegisterModel";

@Injectable()
export class UserService {
  private apiUrl: string = environment.apiHost;

  public username: string = '';

  constructor(private http: HttpClient) {
  }

  public loginUser(formData: FormData): Observable<UserWelcome> {
    return this.http.post<UserWelcome>(`${this.apiUrl}/user/login`, formData, {withCredentials: true});
  }

  public registerUser(registerData: UserRegisterModel) {
    return this.http.post(`${this.apiUrl}/user/register`, registerData, {withCredentials: true});
  }

  public isAdmin(): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/user/admin`, {withCredentials: true});
  }

  public logoutUser() {
    return this.http.post(`${this.apiUrl}/user/logout`, null, {withCredentials: true});
  }

  public createFormData(data: Object): FormData {
    const formData = new FormData();
    for (let key in data) {
      formData.set(key, data[key]);
    }
    return formData;
  }
}
