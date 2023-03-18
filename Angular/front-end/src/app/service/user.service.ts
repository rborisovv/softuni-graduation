import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { environment } from "../../environments/environment.prod";
import { Observable } from "rxjs";
import { UserWelcome } from "../interface/user.welcome";
import { IUserRegisterModel } from "../authentication/register/IUserRegisterModel";
import { HttpResponse } from "../interface/http.response";
import { Product } from "../interface/product";
import { Checkout } from "../interface/checkout";
import { Order } from "../interface/order";
import { PasswordReset } from "../interface/passwordReset";

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

  loadFavouriteProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/user/favourites`);
  }

  loadBasketProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/user/basket`);
  }

  removeFromFavourites(identifier: string): Observable<HttpResponse> {
    return this.http.post<HttpResponse>(`${this.apiUrl}/user/removeFromFavourites`, identifier);
  }

  addToBasket(identifier: string): Observable<HttpResponse> {
    return this.http.post<HttpResponse>(`${this.apiUrl}/user/addToBasket`, identifier);
  }

  removeFromBasket(identifier: string): Observable<HttpResponse> {
    return this.http.post<HttpResponse>(`${this.apiUrl}/user/removeFromBasket`, identifier);
  }

  updateBasketProductQuantity(identifier: string, quantity: number): Observable<HttpResponse> {
    return this.http.post<HttpResponse>(`${this.apiUrl}/user/updateBasketProduct`, { identifier, quantity });
  }

  submitCheckoutFlow(checkout: Checkout): Observable<Order> {
    return this.http.post<Order>(`${this.apiUrl}/user/submitCheckoutFlow`, checkout);
  }

  fetchCheckoutDataIfPresent(): Observable<Checkout> {
    return this.http.get<Checkout>(`${this.apiUrl}/user/fetchCheckoutData`);
  }

  createOrder(): Observable<HttpResponse> {
    return this.http.post<HttpResponse>(`${this.apiUrl}/user/createOrder`, null);
  }

  resetPassword(email: string): Observable<HttpResponse> {
    return this.http.post<HttpResponse>(`${this.apiUrl}/auth/resetPassword`, email);
  }

  changePassword(data: PasswordReset): Observable<HttpResponse> {
    return this.http.post<HttpResponse>(`${this.apiUrl}/auth/changePassword`, data);
  }

  hasActivePasswordRequest(token: string): Observable<boolean> {
    return this.http.post<boolean>(`${this.apiUrl}/auth/hasActivePasswordRequest`, token);
  }
}
