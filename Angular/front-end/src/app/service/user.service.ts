import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment.prod";
import {Observable} from "rxjs";
import {UserWelcome} from "../interface/user.welcome";
import {IUserRegisterModel} from "../authentication/register/IUserRegisterModel";

@Injectable()
export class UserService {
  private apiUrl: string = environment.apiHost;

  public username: string = '';

  constructor(private http: HttpClient) {
  }

  public loginUser(formData: FormData): Observable<UserWelcome> {
    return this.http.post<UserWelcome>(`${this.apiUrl}/auth/login`, formData, {withCredentials: true});
  }

  public registerUser(registerData: IUserRegisterModel) {
    return this.http.post(`${this.apiUrl}/auth/register`, registerData, {withCredentials: true});
  }

  public isAdmin(): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/auth/admin`, {withCredentials: true});
  }

  public isEmailPresent(email: string): Observable<boolean> {
    return this.http.post<boolean>(`${this.apiUrl}/auth/email`, email, {withCredentials: true});
  }

  public isUsernamePresent(username: string): Observable<boolean> {
    return this.http.post<boolean>(`${this.apiUrl}/auth/username`, username, {withCredentials: true});
  }

  public logoutUser() {
    return this.http.post(`${this.apiUrl}/auth/logout`, null, {withCredentials: true});
  }

  public obtainCsrf() {
    return this.http.get(`${this.apiUrl}/auth/csrf`);
  }
}
