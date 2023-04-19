import { Injectable } from '@angular/core';
import { NetworkType } from '@api/custom';
import { ReplaySubject } from 'rxjs';
import { BehaviorSubject } from 'rxjs';
import { Observable } from 'rxjs';
import { NodeClick } from '../domain';
import { PoiClick } from '../domain';
import { RouteClick } from '../domain';

@Injectable()
export class MapService {
  highlightedNodeId$: Observable<string>;
  highlightedRouteId$: Observable<string>;
  networkType$: Observable<NetworkType>;
  popupType$: Observable<string>;
  poiClicked$: Observable<PoiClick>;
  nodeClicked$: Observable<NodeClick>;
  routeClicked$: Observable<RouteClick>;

  private _highlightedNodeId$ = new BehaviorSubject<string>(null);
  private _highlightedRouteId$ = new BehaviorSubject<string>(null);
  private _networkType$ = new BehaviorSubject<NetworkType | null>(null);
  private _popupType$ = new BehaviorSubject<string>('');
  private _poiClicked$ = new ReplaySubject<PoiClick>(1);
  private _nodeClicked$ = new ReplaySubject<NodeClick>(1);
  private _routeClicked$ = new ReplaySubject<RouteClick>(1);

  constructor() {
    this.highlightedNodeId$ = this._highlightedNodeId$;
    this.highlightedRouteId$ = this._highlightedRouteId$;
    this.networkType$ = this._networkType$.asObservable();
    this.popupType$ = this._popupType$.asObservable();
    this.poiClicked$ = this._poiClicked$.asObservable();
    this.nodeClicked$ = this._nodeClicked$.asObservable();
    this.routeClicked$ = this._routeClicked$.asObservable();
  }

  networkType(): NetworkType {
    return this._networkType$.value;
  }

  nextNetworkType(networkType: NetworkType): void {
    return this._networkType$.next(networkType);
  }

  nextPoiClick(poiClick: PoiClick) {
    this._popupType$.next('poi');
    this._poiClicked$.next(poiClick);
  }

  nextNodeClick(nodeClick: NodeClick) {
    this._popupType$.next('node');
    this._nodeClicked$.next(nodeClick);
  }

  nextRouteClick(routeClick: RouteClick) {
    this._popupType$.next('route');
    this._routeClicked$.next(routeClick);
  }
}
