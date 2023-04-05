import { Injectable } from '@angular/core';
import { environment } from "../../environments/environment.prod";
import { HttpClient } from "@angular/common/http";
import { Observable, shareReplay } from "rxjs";
import { HttpResponse } from "../interface/http.response";
import { PageableData } from "../model/pageable.data";
import { ProductPageable } from "../model/product.pageable";

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  apiUrl = environment.apiHost;

  constructor(private http: HttpClient) {

  }

  public isProductByNamePresent(name: string): Observable<boolean> {
    return this.http.post<boolean>(`${this.apiUrl}/product/findByName`, name);
  }

  public isProductByIdentifierPresent(identifier: string): Observable<boolean> {
    return this.http.post<boolean>(`${this.apiUrl}/product/findByIdentifier`, identifier);
  }

  public createProduct(product: FormData): Observable<HttpResponse> {
    return this.http.post<HttpResponse>(`${this.apiUrl}/product/create`, product);
  }

  public loadAllProducts(data: PageableData): Observable<ProductPageable> {
    return this.http.post<ProductPageable>(`${this.apiUrl}/product/findAll`, data).pipe(shareReplay());
  }

  public deleteProduct(identifier: string): Observable<HttpResponse> {
    return this.http.delete<HttpResponse>(`${this.apiUrl}/product/delete/${identifier}`);
  }
}
