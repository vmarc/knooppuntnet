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
    return this.http.get<Route>(`/api/planner/cycling/route?nodes=${selected.selectedNodesByUser}`);
  }

  getCyclingNodesFromMultiline(routeId: string, latestNodeId: number): Observable<SelectedRoute> {
    return this.http.get<SelectedRoute>(`/api/planner/cycling/${routeId}/${latestNodeId}`)
  }

  calculateHikingRoute(selected: SelectedRoute): Observable<Route> {
    return this.http.get<Route>(`/api/planner/hiking/route?nodes=${selected.selectedNodesByUser}`);
  }

  getHikingNodesFromMultiline(routeId: string, latestNodeId: number): Observable<SelectedRoute> {
    return this.http.get<SelectedRoute>(`/api/planner/hiking/${routeId}/${latestNodeId}`)
  }
}
