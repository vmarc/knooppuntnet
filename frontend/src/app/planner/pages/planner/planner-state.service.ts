import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { computed } from '@angular/core';
import { Params } from '@angular/router';
import { Router } from '@angular/router';
import { RouteType } from '@api/common/route-type';
import { RouteTypes } from '@app/kpn/common';
import { MapLayerState } from '@app/ol/domain';
import { MapPosition } from '@app/ol/domain';
import { OldPoiTileLayerService } from '@app/ol/services';
import { MapMode } from '@app/ol/services';
import { BrowserStorageService } from '@app/services';
import { Util } from '@app/shared/components/util';
import { State } from '@app/state';
import { Coordinate } from 'ol/coordinate';
import { fromLonLat } from 'ol/proj';
import { from } from 'rxjs';
import { Observable } from 'rxjs';
import { MapResultMode } from '../../../ol/services/map-result-mode';
import { RouterService } from '../../../shared/services/router.service';

@Injectable()
export class PlannerStateService {
  private readonly state = inject(State);
  private readonly routerService = inject(RouterService);
  private readonly router = inject(Router);
  private readonly browserStorageService = inject(BrowserStorageService);

  private readonly plannerPositionKey = 'planner-position';

  private readonly defaultPoiLayerStates: MapLayerState[] = [
    { id: 'hiking-biking', name: 'TODO TRANSLATION', enabled: true, visible: true },
    { id: 'landmarks', name: 'TODO TRANSLATION', enabled: true, visible: true },
    { id: 'restaurants', name: 'TODO TRANSLATION', enabled: true, visible: true },
    { id: 'places-to-stay', name: 'TODO TRANSLATION', enabled: true, visible: true },
    { id: 'tourism', name: 'TODO TRANSLATION', enabled: true, visible: true },
    { id: 'amenity', name: 'TODO TRANSLATION', enabled: true, visible: false },
    { id: 'shops', name: 'TODO TRANSLATION', enabled: true, visible: false },
    { id: 'foodshops', name: 'TODO TRANSLATION', enabled: true, visible: false },
    { id: 'sports', name: 'TODO TRANSLATION', enabled: true, visible: false },
  ];

  readonly poisVisible = computed(() => {
    let visible = false;
    const poiLayerState = this.state.planner
      .layerStates()
      .find((layerState) => layerState.id == OldPoiTileLayerService.poiLayerId);
    if (poiLayerState) {
      visible = poiLayerState.visible;
    }
    return visible;
  });

  poiGroupVisible(layerId: string): boolean {
    const layerStates = this.state.planner
      .poiLayerStates()
      .filter((layerState) => layerState.id === layerId);
    return layerStates.length === 1 && layerStates[0].visible;
  }

  onInit(): void {
    const uniqueQueryParams = Util.uniqueParams(this.routerService.queryParams());
    this.updatePlannerState(this.routerService.params(), uniqueQueryParams);
  }

  private updatePlannerState(routeParams: Params, queryParams: Params): void {
    const routeType = this.parseRouteType(routeParams);
    this.state.page.updateRouteType(routeType);
    this.state.planner.updatePosition(this.parsePosition(queryParams));
    this.state.planner.updateMapMode(this.parseMapMode(queryParams));
    this.state.planner.updateResultMode(this.parseResultMode(queryParams));

    let urlLayerIds: string[] = [];
    const layersParam = queryParams['layers'];
    if (layersParam) {
      urlLayerIds = layersParam.split(',');
    }
    this.state.planner.updateUrlLayerIds(urlLayerIds);

    this.state.planner.updateLayerStates([]);
    this.state.planner.updatePoiLayerStates(this.parsePoiLayerStates(queryParams));
  }

  private parseRouteType(queryParams: Params): RouteType {
    const routeTypeParam = queryParams['routeType'];
    if (routeTypeParam) {
      const routeType = RouteTypes.withName(routeTypeParam);
      if (routeType) {
        return routeType;
      }
    }
    return 'hiking';
  }

  private parsePosition(queryParams: Params): MapPosition {
    const positionParam = queryParams['position'];
    let position = MapPosition.fromQueryParam(positionParam);
    if (!position) {
      const mapPositionString = this.browserStorageService.get(this.plannerPositionKey);
      if (mapPositionString) {
        position = MapPosition.fromQueryParam(mapPositionString);
      } else {
        // TODO replace temporary code
        const a: Coordinate = fromLonLat([2.24, 50.16]);
        const b: Coordinate = fromLonLat([10.56, 54.09]);
        // const extent: Extent = [a[0], a[1], b[0], b[1]];
        const x = (b[0] - a[0]) / 2 + a[0];
        const y = (b[1] - a[1]) / 2 + a[1];
        position = new MapPosition(14, x, y, 0);
      }
    }
    return position;
  }

  private parseMapMode(queryParams: Params): MapMode {
    const mapModeParam = queryParams['mode'];
    let mapMode: MapMode = 'surface';
    if (mapModeParam === 'survey') {
      mapMode = 'survey';
    } else if (mapModeParam === 'analysis') {
      mapMode = 'analysis';
    }
    return mapMode;
  }

  private parseResultMode(queryParams: Params): MapResultMode {
    const resultModeParam = queryParams['result'];
    let resultMode: MapResultMode = 'compact';
    if (resultModeParam === 'detailed') {
      resultMode = 'detailed';
    }
    return resultMode;
  }

  private parsePoiLayerStates(queryParams: Params): MapLayerState[] {
    const poiLayersParam = queryParams['poi-layers'];
    let poiLayerStates: MapLayerState[];
    if (poiLayersParam) {
      poiLayerStates = this.defaultPoiLayerStates.map((defaultLayerState) => {
        const visible = poiLayersParam.includes(defaultLayerState.id);
        return {
          ...defaultLayerState,
          visible,
        };
      });
    } else {
      poiLayerStates = this.defaultPoiLayerStates;
    }
    return poiLayerStates;
  }

  private toQueryParams(): Params {
    const position = MapPosition.toQueryParam(this.state.planner.position());
    const mode = this.state.planner.mapMode();
    const result = this.state.planner.resultMode();
    const layers = this.state.planner
      .layerStates()
      .filter((layerState) => layerState.visible)
      .map((layerState) => layerState.id)
      .join(',');
    const poiLayers = this.state.planner
      .poiLayerStates()
      .filter((layerState) => layerState.visible)
      .map((layerState) => layerState.id)
      .join(',');

    return {
      mode,
      position,
      result,
      layers,
      'poi-layers': poiLayers,
    };
  }

  // TODO update url after changing planner state
  private navigate(): Observable<boolean> {
    const queryParams = this.toQueryParams();
    const promise = this.router.navigate(['map', this.state.page.routeType], {
      queryParams,
      replaceUrl: true, // do not push a new entry to the browser history
    });
    return from(promise);
  }
}
