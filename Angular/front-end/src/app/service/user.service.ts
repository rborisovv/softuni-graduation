import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {IUserLogin} from "../interface/user-login";
import {environment} from "../../environments/environment.prod";
import {Observable} from "rxjs";

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
