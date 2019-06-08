import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Route, SelectedRoute} from "../model";
import {Observable} from "rxjs";

@Injectable({
  providedIn: "root"
})
export class RouteService {

  constructor(private http: HttpClient) {
  }

  calculateCyclingRoute(selected: SelectedRoute): Observable<Route> {
    return this.http.get<Route>(`/api/cycling/route?nodes=${selected.selectedNodesByUser}`);
  }

  getCyclingNodesFromMultiline(routeId: string, latestNodeId: number): Observable<SelectedRoute> {
    return this.http.get<SelectedRoute>(`/api/cycling/${routeId}/${latestNodeId}`)
  }

  calculateHikingRoute(selected: SelectedRoute): Observable<Route> {
    return this.http.get<Route>(`/api/hiking/route?nodes=${selected.selectedNodesByUser}`);
  }

  getHikingNodesFromMultiline(routeId: string, latestNodeId: number): Observable<SelectedRoute> {
    return this.http.get<SelectedRoute>(`/api/hiking/${routeId}/${latestNodeId}`)
  }
}
