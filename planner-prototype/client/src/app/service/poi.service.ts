import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {Poi} from "../model";

@Injectable({
  providedIn: 'root'
})
export class PoiService {

  constructor(private http: HttpClient) {
  }

  getPoiInformation(type: string, poiId: number): Observable<Poi> {
    return this.http.get<Poi>(environment.knooppuntUrl + "/poi/" + type + "/" + poiId);
  }
}
