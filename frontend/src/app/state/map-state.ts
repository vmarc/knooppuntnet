import { Signal } from '@angular/core';
import { computed } from '@angular/core';
import { signal } from '@angular/core';
import { MapLayerState } from '@app/ol/domain';
import { Coordinate } from 'ol/coordinate';
import { BackgroundLayerType } from '../map/layers/background-layer-type';
import { LayerType } from '../map/layers/layer-type';
import { PoiStyleMap } from '../map/style/poi-style-map';
import { MapStyleOptions } from './map-style-options';
import { MapRoutePopupState } from './map-route-popup-state';

export class MapState {
  private readonly _viewZoom = signal<number>(15);
  private readonly _center = signal<Coordinate | null>(null);
  private readonly _routePopupState = signal<MapRoutePopupState>(
    new MapRoutePopupState([], [0, 0])
  );
  private readonly _mode = signal<string>('analysis');
  private readonly _scopeInternational = signal<boolean>(true);
  private readonly _scopeNational = signal<boolean>(true);
  private readonly _scopeRegional = signal<boolean>(true);
  private readonly _scopeLocal = signal<boolean>(true);
  private readonly _scopeNodeRoutes = signal<boolean>(true);
  private readonly _selectedRoute = signal<number | undefined>(undefined);
  private readonly _poiStyleMap = signal<PoiStyleMap>(undefined);
  private readonly _poiActive = signal<ReadonlyMap<string, boolean>>(new Map());
  private readonly _poiLayerStates = signal<ReadonlyArray<MapLayerState>>([]);

  private readonly _backgroundLayer = signal<BackgroundLayerType>('standard');
  private readonly _routeLayerEnabled = signal<boolean>(true);
  private readonly _poiLayerEnabled = signal<boolean>(true);
  private readonly _gridLayerEnabled = signal<boolean>(true);
  private readonly _flandersOpenDataLayerEnabled = signal<boolean>(true);
  private readonly _netherlandsOpenDataLayerEnabled = signal<boolean>(true);
  private readonly _franceOpenDataLayerEnabled = signal<boolean>(true);

  readonly zoom = computed(() => Math.floor(this._viewZoom()));
  readonly center = this._center.asReadonly();
  readonly routePopupState = this._routePopupState.asReadonly();
  readonly mode = this._mode.asReadonly();
  readonly scopeInternational = this._scopeInternational.asReadonly();
  readonly scopeNational = this._scopeNational.asReadonly();
  readonly scopeRegional = this._scopeRegional.asReadonly();
  readonly scopeLocal = this._scopeLocal.asReadonly();
  readonly scopeNodeRoutes = this._scopeNodeRoutes.asReadonly();
  readonly selectedRoute = this._selectedRoute.asReadonly();
  readonly poiStyleMap = this._poiStyleMap.asReadonly();
  readonly poiActive = this._poiActive.asReadonly();
  readonly poiLayerStates = this._poiLayerStates.asReadonly();

  readonly standardBackgroundLayerEnabled = computed(() => this._backgroundLayer() === 'standard');
  readonly osmBackgroundLayerEnabled = computed(() => this._backgroundLayer() === 'osm');
  readonly routeLayerEnabled = this._routeLayerEnabled.asReadonly();
  readonly poiLayerEnabled = this._poiLayerEnabled.asReadonly();
  readonly gridLayerEnabled = this._gridLayerEnabled.asReadonly();
  readonly flandersOpenDataLayerEnabled = this._flandersOpenDataLayerEnabled.asReadonly();
  readonly netherlandsOpenDataLayerEnabled = this._netherlandsOpenDataLayerEnabled.asReadonly();
  readonly franceOpenDataLayerEnabled = this._franceOpenDataLayerEnabled.asReadonly();

  readonly mapStyleOptions = computed(() => {
    const options: MapStyleOptions = {
      zoom: this.zoom(),
      mode: this.mode(),
      scopeInternational: this.scopeInternational(),
      scopeNational: this.scopeNational(),
      scopeRegional: this.scopeRegional(),
      scopeLocal: this.scopeLocal(),
      scopeNodeRoutes: this.scopeNodeRoutes(),
      selectedRoute: this.selectedRoute(),
    };
    return options;
  });

  readonly layerEnabled: Signal<ReadonlyMap<LayerType, boolean>> = computed(
    () =>
      new Map([
        ['osm-background', this.osmBackgroundLayerEnabled()],
        ['standard-background', this.standardBackgroundLayerEnabled()],
        ['route', this.routeLayerEnabled()],
        ['poi', this.poiLayerEnabled()],
        ['grid', this.gridLayerEnabled()],
        ['flanders-open-data', this.flandersOpenDataLayerEnabled()],
        ['netherlands-open-data', this.netherlandsOpenDataLayerEnabled()],
        ['france-open-data', this.franceOpenDataLayerEnabled()],
      ])
  );

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

  updateScopeInternational(value: boolean): void {
    this._scopeInternational.set(value);
  }

  updateScopeNational(value: boolean): void {
    this._scopeNational.set(value);
  }

  updateScopeRegional(value: boolean): void {
    this._scopeRegional.set(value);
  }

  updateScopeLocal(value: boolean): void {
    this._scopeLocal.set(value);
  }

  updateScopeNodeRoutes(value: boolean): void {
    this._scopeNodeRoutes.set(value);
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

  updateOsmBackgroundLayerEnabled(enabled: boolean): void {
    this._backgroundLayer.set(enabled ? 'osm' : 'none');
  }

  updateStandardBackgroundLayerEnabled(enabled: boolean): void {
    this._backgroundLayer.set(enabled ? 'standard' : 'none');
  }

  updateRouteLayerEnabled(value: boolean): void {
    this._routeLayerEnabled.set(value);
  }

  updatePoiLayerEnabled(value: boolean): void {
    this._poiLayerEnabled.set(value);
  }

  updateGridLayerEnabled(value: boolean): void {
    this._gridLayerEnabled.set(value);
  }

  updateFlandersOpenDataLayerEnabled(value: boolean): void {
    this._flandersOpenDataLayerEnabled.set(value);
  }

  updateNetherlandsOpenDataLayerEnabled(value: boolean): void {
    this._netherlandsOpenDataLayerEnabled.set(value);
  }

  updateFranceOpenDataLayerEnabled(value: boolean): void {
    this._franceOpenDataLayerEnabled.set(value);
  }
}
