import { WritableSignal } from '@angular/core';
import { Signal } from '@angular/core';
import { computed } from '@angular/core';
import { signal } from '@angular/core';
import { LayerType } from './layer-type';

export class MapStateLayers {
  private readonly _backgroundLayerEnabled: WritableSignal<boolean>;
  private readonly _routeLayerEnabled: WritableSignal<boolean>;
  private readonly _poiLayerEnabled: WritableSignal<boolean>;
  private readonly _gridLayerEnabled: WritableSignal<boolean>;
  private readonly _flandersOpenDataLayerEnabled: WritableSignal<boolean>;
  private readonly _netherlandsOpenDataLayerEnabled: WritableSignal<boolean>;
  private readonly _franceOpenDataLayerEnabled: WritableSignal<boolean>;
  private readonly _monitorLayerEnabled: WritableSignal<boolean>;

  readonly backgroundLayerEnabled: Signal<boolean>;
  readonly routeLayerEnabled: Signal<boolean>;
  readonly poiLayerEnabled: Signal<boolean>;
  readonly gridLayerEnabled: Signal<boolean>;
  readonly flandersOpenDataLayerEnabled: Signal<boolean>;
  readonly netherlandsOpenDataLayerEnabled: Signal<boolean>;
  readonly franceOpenDataLayerEnabled: Signal<boolean>;
  readonly monitorLayerEnabled: Signal<boolean>;

  readonly layerEnabledMap: Signal<ReadonlyMap<LayerType, boolean>>;

  constructor() {
    // TODO redesign - add initial values based on local storage and query params

    this._backgroundLayerEnabled = signal<boolean>(true);
    this._routeLayerEnabled = signal<boolean>(true);
    this._poiLayerEnabled = signal<boolean>(false);
    this._gridLayerEnabled = signal<boolean>(false);
    this._flandersOpenDataLayerEnabled = signal<boolean>(false);
    this._netherlandsOpenDataLayerEnabled = signal<boolean>(false);
    this._franceOpenDataLayerEnabled = signal<boolean>(false);
    this._monitorLayerEnabled = signal<boolean>(false);

    this.backgroundLayerEnabled = this._backgroundLayerEnabled.asReadonly();
    this.routeLayerEnabled = this._routeLayerEnabled.asReadonly();
    this.poiLayerEnabled = this._poiLayerEnabled.asReadonly();
    this.gridLayerEnabled = this._gridLayerEnabled.asReadonly();
    this.flandersOpenDataLayerEnabled = this._flandersOpenDataLayerEnabled.asReadonly();
    this.netherlandsOpenDataLayerEnabled = this._netherlandsOpenDataLayerEnabled.asReadonly();
    this.franceOpenDataLayerEnabled = this._franceOpenDataLayerEnabled.asReadonly();
    this.monitorLayerEnabled = this._monitorLayerEnabled.asReadonly();

    this.layerEnabledMap = computed(
      () =>
        new Map([
          ['route', this.routeLayerEnabled()],
          ['poi', this.poiLayerEnabled()],
          ['grid', this.gridLayerEnabled()],
          ['flanders-open-data', this.flandersOpenDataLayerEnabled()],
          ['netherlands-open-data', this.netherlandsOpenDataLayerEnabled()],
          ['france-open-data', this.franceOpenDataLayerEnabled()],
          ['monitor', this.monitorLayerEnabled()],
        ])
    );
  }

  updateBackgroundLayerEnabled(enabled: boolean): void {
    this._backgroundLayerEnabled.set(enabled);
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

  updateMonitorLayerEnabled(value: boolean): void {
    this._monitorLayerEnabled.set(value);
  }
}
