import { Injectable } from '@angular/core';
import { environment } from "../../environments/environment.prod";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

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
}
