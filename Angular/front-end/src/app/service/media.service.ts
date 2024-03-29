import { Injectable } from '@angular/core';
import { environment } from "../../environments/environment.prod";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Media } from "../model/media";
import { HttpResponse } from "../interface/http.response";
import { MediaPageable } from "../model/media.pageable";
import { PageableData } from "../model/pageable.data";

@Injectable({
  providedIn: 'root'
})
export class MediaService {
  apiUrl = environment.apiHost;

  constructor(private http: HttpClient) {
  }

  public filterMediaByName(name: string): Observable<Media[]> {
    return this.http.post<Media[]>(`${this.apiUrl}/media/filter`, name);
  }

  public isMediaByNamePresent(name: string): Observable<boolean> {
    return this.http.post<boolean>(`${this.apiUrl}/media/findByName`, name);
  }

  public createMedia(formData: FormData): Observable<HttpResponse> {
    return this.http.post<HttpResponse>(`${this.apiUrl}/media/create`, formData);
  }

  public fetchMedia(identifier: string) {
    return this.http.get(`${this.apiUrl}/media/sys_master/h4f/${identifier}.jpg`);
  }

  public fetchAllMedias(pageableData: PageableData): Observable<MediaPageable> {
    return this.http.post<MediaPageable>(`${this.apiUrl}/media/findAll`, pageableData);
  }

  public deleteMedia(pk: string): Observable<HttpResponse> {
    return this.http.delete<HttpResponse>(`${this.apiUrl}/media/delete/${pk}`);
  }
}
