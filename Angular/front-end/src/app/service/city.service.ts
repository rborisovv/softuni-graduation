import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {City} from "../interface/city";
import {environment} from "../../environments/environment.prod";

@Injectable()
export class CityService {
  private apiUrl: string = environment.apiHost;

  constructor(private http: HttpClient) { }

  public findAllCities(): Observable<City[]> {
    return this.http.get<City[]>(`${this.apiUrl}/city/all`, {withCredentials: true});
  }

  public findCityByName(name: string): Observable<City> {
    return this.http.get<City>(`${this.apiUrl}/city/${name}`, {withCredentials: true});
  }
}
