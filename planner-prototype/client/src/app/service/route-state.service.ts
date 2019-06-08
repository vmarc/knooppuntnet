import {Injectable} from "@angular/core";
import {RouteState} from "../model";

@Injectable({
  providedIn: "root"
})
export class RouteStateService {

  saveState: boolean = false;
  routeStates: RouteState[] = [];

  constructor() {
  }

  addRouteState(routeState: RouteState) {
    if (this.saveState && routeState.selectedRoute.selectedNodesByUser.length >= 2) {
      const newState = new RouteState();

      routeState.selectedRoute.selectedNodesByUser.forEach(nodes => {
        newState.selectedRoute.selectedNodesByUser.push(nodes);
      });

      routeState.selectedRoute.selectedNamesByUser.forEach(names => {
        newState.selectedRoute.selectedNamesByUser.push(names);
      });

      routeState.selectedFeatures.forEach(f => {
        let feature = f.clone();
        newState.selectedFeatures.push(feature);
      });

      this.routeStates.push(newState);
    }
  }

  clearStates() {
    this.routeStates = [];
    this.saveState = false;
  }

  returnPreviousRouteState(): RouteState {
    if (this.routeStates.length === 1) {
      this.saveState = false;
    }
    if (this.routeStates.length === 0) {
      this.saveState = false;
      return new RouteState();
    }
    return this.routeStates.pop();
  }
}
