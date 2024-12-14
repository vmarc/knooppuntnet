import { computed } from '@angular/core';
import { signal } from '@angular/core';
import { SurveyDateValues } from '@app/core';
import { MapLayerState } from '@app/ol/domain';
import { Coordinate } from 'ol/coordinate';
import { PoiStyleMap } from '../map/style/poi-style-map';
import { MapStateLayers } from './map-state-layers';
import { MapStateScopes } from './map-state-scopes';
import { MapStyleOptions } from './map-style-options';
import { MapRoutePopupState } from './map-route-popup-state';

export class MapState {
  private readonly _viewZoom = signal<number>(15);
  private readonly _center = signal<Coordinate | null>(null);
  private readonly _routePopupState = signal<MapRoutePopupState>(
    new MapRoutePopupState([], [0, 0])
  );
  private readonly _mode = signal<string>('survey');

  private readonly _selectedRoute = signal<number | undefined>(undefined);
  private readonly _poiStyleMap = signal<PoiStyleMap>(undefined);
  private readonly _poiActive = signal<ReadonlyMap<string, boolean>>(new Map());
  private readonly _poiLayerStates = signal<ReadonlyArray<MapLayerState>>([]);
  private readonly _surveyDateValues = signal<SurveyDateValues | undefined>(undefined);

  readonly layers: MapStateLayers;
  readonly scopes: MapStateScopes;

  readonly zoom = computed(() => Math.floor(this._viewZoom()));
  readonly center = this._center.asReadonly();
  readonly routePopupState = this._routePopupState.asReadonly();
  readonly mode = this._mode.asReadonly();
  readonly selectedRoute = this._selectedRoute.asReadonly();
  readonly poiStyleMap = this._poiStyleMap.asReadonly();
  readonly poiActive = this._poiActive.asReadonly();
  readonly poiLayerStates = this._poiLayerStates.asReadonly();
  readonly surveyDateValues = this._surveyDateValues.asReadonly();

  readonly mapStyleOptions = computed(() => {
    const options: MapStyleOptions = {
      zoom: this.zoom(),
      mode: this.mode(),
      scopeInternational: this.scopes.scopeInternational(),
      scopeNational: this.scopes.scopeNational(),
      scopeRegional: this.scopes.scopeRegional(),
      scopeLocal: this.scopes.scopeLocal(),
      scopeNodeRoutes: this.scopes.scopeNodeRoutes(),
      selectedRoute: this.selectedRoute(),
      surveyDateValues: this.surveyDateValues(),
    };
    return options;
  });

  constructor() {
    this.layers = new MapStateLayers();
    this.scopes = new MapStateScopes();
  }

  updateViewZoom(value: number): void {
    this._viewZoom.set(value);
  }

  updateCenter(value: Coordinate): void {
    this._center.set(value);
  }

  updateRoutePopupState(value: MapRoutePopupState): void {
    this._routePopupState.set(value);
  }

  updateMode(value: string): void {
    this._mode.set(value);
  }

  updateSelectedRoute(value: number | null): void {
    this._selectedRoute.set(value);
  }

  updatePoiStyleMap(value: PoiStyleMap): void {
    this._poiStyleMap.set(value);
  }

  updatePoiActive(value: ReadonlyMap<string, boolean>): void {
    this._poiActive.set(value);
  }

  updatePoiGroupActive(group: string, active: boolean): void {
    this._poiLayerStates.update((mapLayerStates) => {
      return mapLayerStates.map((mapLayerState) => {
        if (mapLayerState.id === group) {
          return {
            ...mapLayerState,
            visible: active,
          };
        }
        return mapLayerState;
      });
    });
  }

  updateSurveyDateValues(surveyDateValues: SurveyDateValues): void {
    this._surveyDateValues.set(surveyDateValues);
  }
}
