import {Injectable} from "@angular/core";
import {BehaviorSubject, Observable, Subject} from "rxjs";
import {NetworkType} from "../../kpn/api/custom/network-type";
import {SelectedFeature} from "./domain/selected-feature";
import {PoiClick} from "./domain/poi-click";

@Injectable()
export class MapService {

  highlightedRouteId: string;
  highlightedNodeId: string;
  selectedRouteId: string;
  selectedNodeId: string;

  networkType: BehaviorSubject<NetworkType | null> = new BehaviorSubject(null);
  selectedFeature: BehaviorSubject<SelectedFeature> = new BehaviorSubject(null);
  _poiClickedObserver: Subject<PoiClick> = new Subject(); // not a BehaviorSubject because we do not want subscriber notified upon subscribe

  get poiClickedObserver(): Observable<PoiClick> {
    return this._poiClickedObserver;
  }

  poiClicked(poiClick: PoiClick) {
    this._poiClickedObserver.next(poiClick);
  }

}
