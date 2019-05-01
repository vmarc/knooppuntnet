import {Injectable} from "@angular/core";
import {BehaviorSubject, Observable, Subject} from "rxjs";
import {NetworkType} from "../../kpn/shared/network-type";
import {SelectedFeature} from "./domain/selected-feature";

export class PoiId {
  constructor(readonly elementType: string,
              readonly elementId: number) {
  }
}

@Injectable()
export class MapService {

  highlightedRouteId: string;
  highlightedNodeId: string;
  selectedRouteId: string;
  selectedNodeId: string;

  networkType: BehaviorSubject<NetworkType|null> = new BehaviorSubject(null);
  selectedFeature: BehaviorSubject<SelectedFeature> = new BehaviorSubject(null);
  _poiClickedObserver: Subject<PoiId> = new Subject(); // not a BehaviorSubject because we do not want subscriber notified upon subscribe

  get poiClickedObserver(): Observable<PoiId> {
    return this._poiClickedObserver;
  }

  poiClicked(poiId: PoiId) {
    this._poiClickedObserver.next(poiId);
  }

}
