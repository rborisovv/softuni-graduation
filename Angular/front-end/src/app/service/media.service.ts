import {Injectable} from '@angular/core';
import {environment} from "../../environments/environment.prod";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Media} from "../interface/media";
import {HttpResponse} from "../interface/http.response";

@Injectable({
  providedIn: 'root'
})
export class MediaService {
  apiUrl = environment.apiHost;

  constructor(private http: HttpClient) {
  }

  public filterMediaByName(name: string, typeSubject: string): Observable<Media[]> {
    return this.http.post<Media[]>(`${this.apiUrl}/media/filter/${typeSubject}`, name, {withCredentials: true});
  }

  public isMediaByNamePresent(name: string): Observable<boolean> {
    return this.http.post<boolean>(`${this.apiUrl}/media/findByName`, name, {withCredentials: true});
  }

  public createMedia(formData: FormData): Observable<HttpResponse> {
    return this.http.post<HttpResponse>(`${this.apiUrl}/media/create`, formData, {withCredentials: true});
  }

  public fetchMedia(identifier: string) {
    return this.http.get(`${this.apiUrl}/media/sys_master/h4f/${identifier}.jpg`);
  }
}
