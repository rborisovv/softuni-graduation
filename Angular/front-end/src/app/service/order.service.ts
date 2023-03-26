import { Injectable } from '@angular/core';
import { environment } from "../../environments/environment.prod";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Orders } from "../interface/orders";

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  apiUrl = environment.apiHost;

  constructor(private http: HttpClient) {

  }

  public canActivateOrderConfirmationFlow(): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/order/canActivateOrderFlow`);
  }

  public fetchAllOrders(): Observable<Orders[]> {
    return this.http.get<Orders[]>(`${this.apiUrl}/auth/orders`);
  }
}
