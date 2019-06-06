import {Injectable} from "@angular/core";

@Injectable()
export class MapState {

  highlightedRouteId?: string = '';
  highlightedNodeId?: string = '';
  selectedRouteId?: string = '';
  selectedNodeId?: string = '';

  constructor() {
  }
}
