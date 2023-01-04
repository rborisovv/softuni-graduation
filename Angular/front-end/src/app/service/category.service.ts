import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment.prod";
import {Observable} from "rxjs";
import {HttpResponse} from "../interface/http.response";
import {Category} from "../interface/category";

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  apiUrl = environment.apiHost;

  constructor(private http: HttpClient) {

  }

  public createCategory(formData: FormData): Observable<HttpResponse> {
    return this.http.post<HttpResponse>(`${this.apiUrl}/category/create`, formData, {withCredentials: true});
  }

  public isCategoryByIdentifierPresent(identifier: string) {
    return this.http.post<boolean>(`${this.apiUrl}/category/identifier`, identifier, {withCredentials: true});
  }

  public isCategoryByNamePresent(name: string) {
    return this.http.post<boolean>(`${this.apiUrl}/category/name`, name, {withCredentials: true});
  }

  public loadAllCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(`${this.apiUrl}/category/all`, {withCredentials: true});
  }

  public loadCategory(identifier: string): Observable<Category> {
    return this.http.get<Category>(`${this.apiUrl}/category/${identifier}`, {withCredentials: true});
  }
}
