import {Injectable} from "@angular/core";
import {ReplaySubject} from "rxjs";
import {BehaviorSubject, Observable} from "rxjs";
import {map} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {NodeClick} from "../domain/node-click";
import {PoiClick} from "../domain/poi-click";
import {RouteClick} from "../domain/route-click";
import {MapMode} from "./map-mode";
import {SurveyDateValues} from "./survey-date-values";

@Injectable()
export class MapService {

  highlightedRouteId: string;
  highlightedNodeId: string;
  selectedRouteId: string;
  selectedNodeId: string;

  networkType$: BehaviorSubject<NetworkType | null> = new BehaviorSubject(null);

  mapMode$: Observable<string>;
  surveyDateInfo$: Observable<SurveyDateValues>;
  popupType$: Observable<string>;
  poiClicked$: Observable<PoiClick>;
  nodeClicked$: Observable<NodeClick>;
  routeClicked$: Observable<RouteClick>;

  private _mapMode$ = new BehaviorSubject<string>(MapMode.surface);
  private _surveyDateInfo$ = new BehaviorSubject<SurveyDateValues>(null);
  private _popupType$ = new BehaviorSubject<string>("");
  private _poiClicked$ = new ReplaySubject<PoiClick>(1);
  private _nodeClicked$ = new ReplaySubject<NodeClick>(1);
  private _routeClicked$ = new ReplaySubject<RouteClick>(1);

  constructor(private appService: AppService) {
    appService.surveyDateInfo().pipe(
      map(response => SurveyDateValues.from(response.result))
    ).subscribe(surveyDateValues => this._surveyDateInfo$.next(surveyDateValues));

    this.mapMode$ = this._mapMode$.asObservable();
    this.popupType$ = this._popupType$.asObservable();
    this.poiClicked$ = this._poiClicked$.asObservable();
    this.nodeClicked$ = this._nodeClicked$.asObservable();
    this.routeClicked$ = this._routeClicked$.asObservable();
  }

  mapMode(): string {
    return this._mapMode$.value;
  }

  surveyDateInfo(): SurveyDateValues {
    return this._surveyDateInfo$.value;
  }

  nextMapMode(mapMode: string): void {
    this._mapMode$.next(mapMode);
  }

  nextPoiClick(poiClick: PoiClick) {
    this._popupType$.next("poi");
    this._poiClicked$.next(poiClick);
  }

  nextNodeClick(nodeClick: NodeClick) {
    this._popupType$.next("node");
    this._nodeClicked$.next(nodeClick);
  }

  nextRouteClick(routeClick: RouteClick) {
    this._popupType$.next("route");
    this._routeClicked$.next(routeClick);
  }
}
