import {Injectable} from '@angular/core';
import {ReplaySubject} from 'rxjs';
import {BehaviorSubject, Observable} from 'rxjs';
import {AppService} from '../../../app.service';
import {NetworkType} from '../../../kpn/api/custom/network-type';
import {NodeClick} from '../domain/node-click';
import {PoiClick} from '../domain/poi-click';
import {RouteClick} from '../domain/route-click';
import {MapMode} from './map-mode';
import {SurveyDateValues} from './survey-date-values';

@Injectable()
export class MapService {

  selectedRouteId: string;
  selectedNodeId: string;

  highlightedNodeId$: Observable<string>;
  highlightedRouteId$: Observable<string>;
  networkType$: Observable<NetworkType>;
  mapMode$: Observable<MapMode>;
  surveyDateInfo$: Observable<SurveyDateValues>;
  popupType$: Observable<string>;
  poiClicked$: Observable<PoiClick>;
  nodeClicked$: Observable<NodeClick>;
  routeClicked$: Observable<RouteClick>;

  private _highlightedNodeId$ = new BehaviorSubject<string>(null);
  private _highlightedRouteId$ = new BehaviorSubject<string>(null);
  private _networkType$ = new BehaviorSubject<NetworkType | null>(null);
  private _mapMode$ = new BehaviorSubject<MapMode>(MapMode.surface);
  private _surveyDateInfo$ = new BehaviorSubject<SurveyDateValues>(null);
  private _popupType$ = new BehaviorSubject<string>('');
  private _poiClicked$ = new ReplaySubject<PoiClick>(1);
  private _nodeClicked$ = new ReplaySubject<NodeClick>(1);
  private _routeClicked$ = new ReplaySubject<RouteClick>(1);

  constructor(private appService: AppService) {
    appService.surveyDateInfo().subscribe(response => {
      if (response.result) {
        this._surveyDateInfo$.next(SurveyDateValues.from(response.result));
      }
    });

    this.highlightedNodeId$ = this._highlightedNodeId$;
    this.highlightedRouteId$ = this._highlightedRouteId$;
    this.networkType$ = this._networkType$.asObservable();
    this.mapMode$ = this._mapMode$.asObservable();
    this.popupType$ = this._popupType$.asObservable();
    this.poiClicked$ = this._poiClicked$.asObservable();
    this.nodeClicked$ = this._nodeClicked$.asObservable();
    this.routeClicked$ = this._routeClicked$.asObservable();
  }

  get highlightedNodeId(): string {
    return this._highlightedNodeId$.value;
  }

  get highlightedRouteId(): string {
    return this._highlightedRouteId$.value;
  }

  nextHighlightedNodeId(value: string): void {
    if (this._highlightedNodeId$.value !== value) {
      this._highlightedNodeId$.next(value);
    }
  }

  nextHighlightedRouteId(value: string): void {
    if (this._highlightedRouteId$.value !== value) {
      this._highlightedRouteId$.next(value);
    }
  }

  networkType(): NetworkType {
    return this._networkType$.value;
  }

  nextNetworkType(networkType: NetworkType): void {
    return this._networkType$.next(networkType);
  }

  get mapMode(): MapMode {
    return this._mapMode$.value;
  }

  surveyDateInfo(): SurveyDateValues {
    return this._surveyDateInfo$.value;
  }

  nextMapMode(mapMode: MapMode): void {
    this._mapMode$.next(mapMode);
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
