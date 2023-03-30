import { Injectable } from '@angular/core';
import { environment } from "../../environments/environment.prod";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Voucher } from "../model/voucher";

@Injectable({
  providedIn: 'root'
})
export class BasketService {
  apiUrl = environment.apiHost;

  constructor(private http: HttpClient) {

  }

  public canActivateCheckout(): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/basket/canActivateCheckout`)
  }

  addVoucherToBasket(voucher: string): Observable<Voucher> {
    return this.http.post<Voucher>(`${this.apiUrl}/basket/addVoucher`, voucher);
  }

  fetchVoucherIfPresent() {
    return this.http.get<Voucher>(`${this.apiUrl}/basket/fetchVoucherIfPresent`);
  }
}
