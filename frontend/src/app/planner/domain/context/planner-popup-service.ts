import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { signal } from '@angular/core';
import { PoiPage } from '@api/common/poi-page';
import { MapNodeDetail } from '@api/common/node/map-node-detail';
import { MapRouteDetail } from '@api/common/route/map-route-detail';
import { ApiResponse } from '@api/custom/api-response';
import { OlUtil } from '@app/ol/ol-util';
import { MapZoomService } from '@app/ol/services/map-zoom.service';
import { ApiService } from '@app/shared/services/api.service';
import { State } from '@app/state/state';
import { Coordinate } from 'ol/coordinate';
import Map from 'ol/Map';
import Overlay from 'ol/Overlay';
import { NodeClick } from '../interaction/actions/node-click';
import { PoiClick } from '../interaction/actions/poi-click';
import { RouteClick } from '../interaction/actions/route-click';
import { PlannerPopup } from './planner-popup';

@Injectable()
export class PlannerPopupService implements PlannerPopup {
  private readonly state = inject(State);

  private readonly apiService = inject(ApiService);
  private readonly mapZoomService = inject(MapZoomService);

  private readonly _routeDetailResponse = signal<ApiResponse<MapRouteDetail>>(null);
  readonly routeDetailResponse = this._routeDetailResponse.asReadonly();

  private readonly _nodeDetailResponse = signal<ApiResponse<MapNodeDetail>>(null);
  readonly nodeDetailResponse = this._nodeDetailResponse.asReadonly();

  private readonly _poiResponse = signal<ApiResponse<PoiPage>>(null);
  readonly poiResponse = this._poiResponse.asReadonly();

  private readonly _popupType = signal<string>('');
  readonly popupType = this._popupType.asReadonly();

  private overlay: Overlay;

  addToMap(map: Map) {
    this.overlay = map.getOverlayById('popup');
  }

  poiClicked(poiClick: PoiClick): void {
    this._popupType.set('poi');
    this.apiService
      .poi(poiClick.poiId.elementType, poiClick.poiId.elementId)
      .subscribe((response) => {
        this._poiResponse.set(response);
        setTimeout(() => this.setPosition(poiClick.coordinate, -45), 0);
      });
  }

  nodeClicked(nodeClick: NodeClick): void {
    this._popupType.set('node');
    const routeType = this.state.page.routeType();
    const nodeId = +nodeClick.node.node.nodeId;
    this.apiService.mapNodeDetail(routeType, nodeId).subscribe((response) => {
      this._nodeDetailResponse.set(response);
      if (response.result) {
        const coordinate = OlUtil.toCoordinate(response.result.latitude, response.result.longitude);
        const verticalOffset = this.mapZoomService.zoomLevel() <= 13 ? -13 : -24;
        setTimeout(() => this.setPosition(coordinate, verticalOffset), 0);
      }
    });
  }

  routeClicked(routeClick: RouteClick): void {
    this.apiService.mapRouteDetail(routeClick.route.routeId).subscribe((response) => {
      if (response.result) {
        this._routeDetailResponse.set(response);
        setTimeout(() => this.setPosition(routeClick.coordinate, -12), 0);
      }
    });

    this._popupType.set('route');
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
