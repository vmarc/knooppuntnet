import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Route} from "../model";

@Injectable({
  providedIn: 'root'
})
export class GpxService {

  constructor(private http: HttpClient) {
  }

  downloadGPX(route: Route) {
    return this.http.post(environment.knooppuntUrl + '/gpx', route, {responseType: 'blob'});
  }
}
