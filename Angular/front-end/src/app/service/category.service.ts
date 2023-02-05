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
    return this.http.post<HttpResponse>(`${this.apiUrl}/category/create`, formData);
  }

  public isCategoryByIdentifierPresent(identifier: string) {
    return this.http.post<boolean>(`${this.apiUrl}/category/identifier`, identifier);
  }

  public isCategoryByNamePresent(name: string) {
    return this.http.post<boolean>(`${this.apiUrl}/category/name`, name);
  }

  public loadAllCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(`${this.apiUrl}/category/all`);
  }

  public loadCategory(identifier: string): Observable<Category> {
    return this.http.get<Category>(`${this.apiUrl}/category/${identifier}`);
  }

  public updateCategory(formData: FormData): Observable<HttpResponse> {
    return this.http.put<HttpResponse>(`${this.apiUrl}/category/update`, formData);
  }

  public deleteCategory(identifier: string): Observable<HttpResponse> {
    return this.http.delete<HttpResponse>(`${this.apiUrl}/category/delete/${identifier}`);
  }

  public filterCategoriesByName(name: string): Observable<Category[]> {
    return this.http.post<Category[]>(`${this.apiUrl}/category/filterByName`, name);
  }

  loadCategoryWithBreadcrumb(identifier: string): Observable<Category> {
    return this.http.get<Category>(`${this.apiUrl}/category/c/${identifier}`);
  }

  loadCategoryToUpdate(identifier: string) {
    return this.http.get<Category>(`${this.apiUrl}/category/update/${identifier}`);
  }
}
