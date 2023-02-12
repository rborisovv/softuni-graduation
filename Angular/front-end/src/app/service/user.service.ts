import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment.prod";
import {Observable} from "rxjs";
import {UserWelcome} from "../interface/user.welcome";
import {IUserRegisterModel} from "../authentication/register/IUserRegisterModel";
import {HttpResponse} from "../interface/http.response";

@Injectable()
export class UserService {
  private apiUrl: string = environment.apiHost;

  constructor(private http: HttpClient) {
  }

  public loginUser(formData: FormData): Observable<UserWelcome> {
    return this.http.post<UserWelcome>(`${this.apiUrl}/auth/login`, formData);
  }

  public registerUser(registerData: IUserRegisterModel): Observable<IUserRegisterModel> {
    return this.http.post<IUserRegisterModel>(`${this.apiUrl}/auth/register`, registerData);
  }

  public isAdmin(): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/auth/admin`);
  }

  public isEmailPresent(email: string): Observable<boolean> {
    return this.http.post<boolean>(`${this.apiUrl}/auth/email`, email);
  }

  public logoutUser() {
    return this.http.post(`${this.apiUrl}/auth/logout`, null);
  }

  public obtainCsrf() {
    return this.http.get(`${this.apiUrl}/auth/csrf`);
  }

  addToFavourites(identifier: string): Observable<HttpResponse> {
    return this.http.post<HttpResponse>(`${this.apiUrl}/user/addToFavourites`, identifier);
  }
}
