import {Injectable} from "@angular/core";
import {BehaviorSubject} from "rxjs";
import {Route, RouteState} from "../model";
import {RouteStateService} from "./route-state.service";

@Injectable({
  providedIn: "root"
})
export class RouteDetailsService {

  private routeState = new BehaviorSubject(new RouteState());
  private route = new BehaviorSubject(null);

  routeObservable = this.route.asObservable();
  routeStateObservable = this.routeState.asObservable();

  constructor(private routeStateService: RouteStateService) {
  }

  displayRoute(route: Route) {
    this.route.next(route);
  }

  clearRoute() {
    this.routeStateService.clearStates();
    this.routeState.next(new RouteState());
    this.route.next(null);
  }

  returnPreviousState() {
    this.route.next(null);
    this.routeState.next(this.routeStateService.returnPreviousRouteState());
  }

  addRouteState(routeState: RouteState) {
    this.routeStateService.addRouteState(routeState);
  }

  updateRoute(routeState: RouteState) {
    this.addRouteState(this.routeState.value);

    this.routeState.next(routeState);
    this.route.next(null);
  }
}
