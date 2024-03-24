import { Injectable } from '@angular/core';
import { signal } from '@angular/core';
import { Coordinate } from 'ol/coordinate';
import Map from 'ol/Map';
import Overlay from 'ol/Overlay';
import { NodeClick } from '../interaction/actions/node-click';
import { PoiClick } from '../interaction/actions/poi-click';
import { RouteClick } from '../interaction/actions/route-click';
import { PlannerPopup } from './planner-popup';

@Injectable()
export class PlannerPopupService implements PlannerPopup {
  private overlay: Overlay;

  private readonly _popupType = signal<string>('');
  private readonly _poiClick = signal<PoiClick>(null);
  private readonly _nodeClick = signal<NodeClick>(null);
  private readonly _routeClick = signal<RouteClick>(null);

  readonly popupType = this._popupType.asReadonly();
  readonly poiClick = this._poiClick.asReadonly();
  readonly nodeClick = this._nodeClick.asReadonly();
  readonly routeClick = this._routeClick.asReadonly();

  addToMap(map: Map) {
    this.overlay = map.getOverlayById('popup');
  }

  poiClicked(poiClick: PoiClick): void {
    this._popupType.set('poi');
    this._poiClick.set(poiClick);
  }

  nodeClicked(nodeClick: NodeClick): void {
    this._popupType.set('node');
    this._nodeClick.set(nodeClick);
  }

  routeClicked(routeClick: RouteClick): void {
    this._popupType.set('route');
    this._routeClick.set(routeClick);
  }

  setPosition(coordinate: Coordinate, verticalOffset: number): void {
    this.overlay.setOffset([0, verticalOffset]);
    this.overlay.setPosition(coordinate);
  }

  reset(): void {
    this.setPosition(undefined, 0);
    this._popupType.set(null);
  }
}
