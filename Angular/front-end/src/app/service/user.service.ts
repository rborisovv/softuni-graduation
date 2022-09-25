import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {IUserLogin} from "../interface/user-login";
import {environment} from "../../environments/environment.prod";
import {Observable} from "rxjs";
import {IUserRegister} from "../interface/user-register";

@Injectable()
export class UserService {
  private apiUrl: string = environment.apiHost;

  constructor(private http: HttpClient) {
  }

  public loginUser(formData: FormData): Observable<IUserLogin> {
    return this.http.post<IUserLogin>(`${this.apiUrl}/user/login`, formData, {withCredentials: true});
  }

  public registerUser(formData: FormData) {
    return this.http.post(`${this.apiUrl}/user/register`, formData);
  }


  // public createFormData(username: string, email: string, firstName: string, lastName: string,
  //                       birthDate: string, password: string, confirmPassword: string): FormData {
  //   const formData = new FormData();
  //   formData.set('username', username);
  //   formData.set('email', email);
  //   formData.set('firstName', firstName);
  //   formData.set('lastName', lastName);
  //   formData.set('birthDate', birthDate);
  //   formData.set('password', password);
  //   formData.set('confirmPassword', confirmPassword);
  //
  //   return formData;
  // }

  public createFormData(data: Object): FormData {
    const formData = new FormData();
    for (let key in data) {
      formData.set(key, data[key]);
    }
    return formData;
  }
}
