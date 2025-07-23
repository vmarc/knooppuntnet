import { WritableSignal } from '@angular/core';
import { Signal } from '@angular/core';
import { computed } from '@angular/core';
import { signal } from '@angular/core';
import { BackgroundLayerType } from './background-layer-type';
import { LayerType } from './layer-type';

export class MapStateLayers {
  private readonly _backgroundLayer: WritableSignal<BackgroundLayerType>;
  private readonly _routeLayerEnabled: WritableSignal<boolean>;
  private readonly _poiLayerEnabled: WritableSignal<boolean>;
  private readonly _gridLayerEnabled: WritableSignal<boolean>;
  private readonly _flandersOpenDataLayerEnabled: WritableSignal<boolean>;
  private readonly _netherlandsOpenDataLayerEnabled: WritableSignal<boolean>;
  private readonly _franceOpenDataLayerEnabled: WritableSignal<boolean>;

  readonly standardBackgroundLayerEnabled: Signal<boolean>;
  readonly osmBackgroundLayerEnabled: Signal<boolean>;
  readonly routeLayerEnabled: Signal<boolean>;
  readonly poiLayerEnabled: Signal<boolean>;
  readonly gridLayerEnabled: Signal<boolean>;
  readonly flandersOpenDataLayerEnabled: Signal<boolean>;
  readonly netherlandsOpenDataLayerEnabled: Signal<boolean>;
  readonly franceOpenDataLayerEnabled: Signal<boolean>;

  readonly layerEnabledMap: Signal<ReadonlyMap<LayerType, boolean>>;

  constructor() {
    // TODO redesign - add initial values based on local storage and query params

    this._backgroundLayer = signal<BackgroundLayerType>('standard');
    this._routeLayerEnabled = signal<boolean>(true);
    this._poiLayerEnabled = signal<boolean>(false);
    this._gridLayerEnabled = signal<boolean>(false);
    this._flandersOpenDataLayerEnabled = signal<boolean>(false);
    this._netherlandsOpenDataLayerEnabled = signal<boolean>(false);
    this._franceOpenDataLayerEnabled = signal<boolean>(false);

    this.standardBackgroundLayerEnabled = computed(() => this._backgroundLayer() === 'standard');
    this.osmBackgroundLayerEnabled = computed(() => this._backgroundLayer() === 'osm');
    this.routeLayerEnabled = this._routeLayerEnabled.asReadonly();
    this.poiLayerEnabled = this._poiLayerEnabled.asReadonly();
    this.gridLayerEnabled = this._gridLayerEnabled.asReadonly();
    this.flandersOpenDataLayerEnabled = this._flandersOpenDataLayerEnabled.asReadonly();
    this.netherlandsOpenDataLayerEnabled = this._netherlandsOpenDataLayerEnabled.asReadonly();
    this.franceOpenDataLayerEnabled = this._franceOpenDataLayerEnabled.asReadonly();
    this.layerEnabledMap = computed(
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
          ['monitor', false],
        ])
    );
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
