import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Route} from "../model";

@Injectable({
  providedIn: "root"
})
export class GpxService {

  constructor(private http: HttpClient) {
  }

  downloadGPX(route: Route) {
    return this.http.post("/api/gpx", route, {responseType: "blob"});
  }
}
