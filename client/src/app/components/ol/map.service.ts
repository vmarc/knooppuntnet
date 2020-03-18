import {Injectable} from "@angular/core";
import {BehaviorSubject, Observable, Subject} from "rxjs";
import {NetworkType} from "../../kpn/api/custom/network-type";
import {NodeClick} from "./domain/node-click";
import {PoiClick} from "./domain/poi-click";
import {RouteClick} from "./domain/route-click";
import {SelectedFeature} from "./domain/selected-feature";

@Injectable()
export class MapService {

  highlightedRouteId: string;
  highlightedNodeId: string;
  selectedRouteId: string;
  selectedNodeId: string;

  networkType: BehaviorSubject<NetworkType | null> = new BehaviorSubject(null);
  selectedFeature: BehaviorSubject<SelectedFeature> = new BehaviorSubject(null);
  popupType = "poi";

  poiClicked: Observable<PoiClick>;
  nodeClicked: Observable<NodeClick>;
  routeClicked: Observable<RouteClick>;

  private _poiClicked: Subject<PoiClick> = new Subject();
  private _nodeClicked: Subject<NodeClick> = new Subject();
  private _routeClicked: Subject<RouteClick> = new Subject();

  constructor() {
    this.poiClicked = this._poiClicked.asObservable();
    this.nodeClicked = this._nodeClicked.asObservable();
    this.routeClicked = this._routeClicked.asObservable();
  }

  nextPoiClick(poiClick: PoiClick) {
    this.popupType = "poi";
    this._poiClicked.next(poiClick);
  }

  nextNodeClick(nodeClick: NodeClick) {
    this.popupType = "node";
    this._nodeClicked.next(nodeClick);
  }

  nextRouteClick(routeClick: RouteClick) {
    this.popupType = "route";
    this._routeClicked.next(routeClick);
  }

}
