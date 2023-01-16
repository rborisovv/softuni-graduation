import {Injectable} from '@angular/core';
import {environment} from "../../environments/environment.prod";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Product} from "../interface/product";
import {HttpResponse} from "../interface/http.response";

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  apiUrl = environment.apiHost;

  constructor(private http: HttpClient) {

  }

  public isProductByNamePresent(name: string): Observable<boolean> {
    return this.http.post<boolean>(`${this.apiUrl}/product/findByName`, name, {withCredentials: true});
  }

  public isProductByIdentifierPresent(identifier: string): Observable<boolean> {
    return this.http.post<boolean>(`${this.apiUrl}/product/findByIdentifier`, identifier, {withCredentials: true});
  }

  public createProduct(product: Product): Observable<HttpResponse> {
    return this.http.post<HttpResponse>(`${this.apiUrl}/product/create`, product, {withCredentials: true});
  }
}
