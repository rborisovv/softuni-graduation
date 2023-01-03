import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment.prod";
import {Category} from "../interface/category";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  apiUrl = environment.apiHost;

  constructor(private http: HttpClient) {
  }

  public createCategory(formData: FormData): Observable<Category> {
    console.log(formData)
    return this.http.post<Category>(`${this.apiUrl}/category/create`, formData, {withCredentials: true});
  }
}
