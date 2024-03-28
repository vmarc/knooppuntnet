import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { computed } from '@angular/core';
import { signal } from '@angular/core';
import { Params } from '@angular/router';
import { Router } from '@angular/router';
import { NetworkType } from '@api/custom';
import { Util } from '@app/components/shared';
import { NetworkTypes } from '@app/kpn/common';
import { MapLayerState } from '@app/ol/domain';
import { MapPosition } from '@app/ol/domain';
import { PoiTileLayerService } from '@app/ol/services';
import { MapMode } from '@app/ol/services';
import { BrowserStorageService } from '@app/services';
import { Coordinate } from 'ol/coordinate';
import { fromLonLat } from 'ol/proj';
import { from } from 'rxjs';
import { Observable } from 'rxjs';
import { MapResultMode } from '../../../ol/services/map-result-mode';
import { RouterService } from '../../../shared/services/router.service';
import { initialPlannerState } from './planner-state';
import { PlannerState } from './planner-state';

@Injectable()
export class PlannerStateService {
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

  private readonly _state = signal<PlannerState>(initialPlannerState);

  readonly state = this._state.asReadonly();
  readonly networkType = computed(() => this._state().networkType);
  readonly position = computed(() => this._state().position);
  readonly mapMode = computed(() => this._state().mapMode);
  readonly resultMode = computed(() => this._state().resultMode);
  readonly resultModeCompact = computed(() => this.resultMode() === 'compact');
  readonly resultModeDetailed = computed(() => this.resultMode() === 'detailed');
  readonly resultModeInstructions = computed(() => this.resultMode() === 'instructions');
  readonly layerStates = computed(() => this._state().layerStates);
  readonly poiLayerStates = computed(() => this._state().poiLayerStates);

  readonly poisVisible = computed(() => {
    let visible = false;
    const poiLayerState = this.layerStates().find(
      (layerState) => layerState.id == PoiTileLayerService.poiLayerId
    );
    if (poiLayerState) {
      visible = poiLayerState.visible;
    }
    return visible;
  });

  poiGroupVisible(layerId: string): boolean {
    const layerStates = this.poiLayerStates().filter((layerState) => layerState.id === layerId);
    return layerStates.length === 1 && layerStates[0].visible;
  }

  onInit(): void {
    const uniqueQueryParams = Util.uniqueParams(this.routerService.queryParams());
    const state = this.toPlannerState(this.routerService.params(), uniqueQueryParams);
    this.updateState(state);
  }

  setNetworkType(networkType: NetworkType): void {
    this.updateState({
      ...this._state(),
      networkType,
    });
  }

  setMapPosition(position: MapPosition): void {
    this.updateState({
      ...this._state(),
      position,
    });
  }

  setMapMode(mapMode: MapMode): void {
    this.updateState({
      ...this._state(),
      mapMode,
    });
  }

  setResultMode(resultMode: MapResultMode): void {
    this.updateState({
      ...this._state(),
      resultMode,
    });
  }

  setLayerStates(layerStates: MapLayerState[]): void {
    this.updateState({
      ...this._state(),
      layerStates,
    });
  }

  setPoiLayerStates(poiLayerStates: MapLayerState[]): void {
    this.updateState({
      ...this._state(),
      poiLayerStates,
    });
  }

  setPoiGroupVisible(groupName: string, visible: boolean): void {
    const poiLayerStates = this.poiLayerStates().map((layerState) => {
      if (layerState.id === groupName) {
        return {
          ...layerState,
          visible,
        };
      }
      return layerState;
    });
    this.updateState({
      ...this._state(),
      poiLayerStates,
    });
  }

  setPoisVisible(visible: boolean): void {
    const layerStates = this.layerStates().map((layerState) => {
      if (layerState.id === PoiTileLayerService.poiLayerId) {
        return {
          ...layerState,
          visible,
        };
      }
      return layerState;
    });
    this.updateState({
      ...this._state(),
      layerStates,
    });
  }

  private updateState(state: PlannerState): void {
    this._state.set(state);
    this.navigate(state);
  }

  private toPlannerState(routeParams: Params, queryParams: Params): PlannerState {
    const networkType = this.parseNetworkType(routeParams);
    const position = this.parsePosition(queryParams);
    const mapMode = this.parseMapMode(queryParams);
    const resultMode = this.parseResultMode(queryParams);

    let urlLayerIds: string[] = [];
    const layersParam = queryParams['layers'];
    if (layersParam) {
      urlLayerIds = layersParam.split(',');
    }
    const layerStates: MapLayerState[] = [];
    const poiLayerStates = this.parsePoiLayerStates(queryParams);

    return {
      networkType,
      position,
      mapMode,
      resultMode,
      urlLayerIds,
      layerStates,
      poiLayerStates,
    };
  }

  private parseNetworkType(queryParams: Params): NetworkType {
    const networkTypeParam = queryParams['networkType'];
    if (networkTypeParam) {
      const networkType = NetworkTypes.withName(networkTypeParam);
      if (networkType) {
        return networkType;
      }
    }
    return NetworkType.hiking;
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
    } else if (resultModeParam === 'instructions') {
      resultMode = 'instructions';
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

  private toQueryParams(state: PlannerState): Params {
    const position = MapPosition.toQueryParam(state.position);
    const mode = state.mapMode;
    const result = state.resultMode;
    const layers = state.layerStates
      .filter((layerState) => layerState.visible)
      .map((layerState) => layerState.id)
      .join(',');
    const poiLayers = state.poiLayerStates
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

  private navigate(state: PlannerState): Observable<boolean> {
    const queryParams = this.toQueryParams(state);
    const promise = this.router.navigate(['map', state.networkType], {
      queryParams,
      replaceUrl: true, // do not push a new entry to the browser history
    });
    return from(promise);
  }
}
