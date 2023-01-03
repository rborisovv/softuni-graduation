import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment.prod";
import {Observable} from "rxjs";
import {HttpResponse} from "../interface/http.response";

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
}
