import { signal } from '@angular/core';
import { MapLayerState } from '@app/ol/domain/map-layer-state';
import { MapPosition } from '@app/ol/domain/map-position';
import { MapMode } from '@app/ol/services/map-mode';
import { MapResultMode } from '../ol/services/map-result-mode';

export class PlannerState {
  private readonly _position = signal<MapPosition | null>(null);
  private readonly _mapMode = signal<MapMode>('surface');
  private readonly _resultMode = signal<MapResultMode>('compact');
  private readonly _layerStates = signal<MapLayerState[]>([]);
  private readonly _urlLayerIds = signal<string[]>([]);
  private readonly _poiLayerStates = signal<MapLayerState[]>([]);

  readonly position = this._position.asReadonly();
  readonly mapMode = this._mapMode.asReadonly();
  readonly resultMode = this._resultMode.asReadonly();
  readonly layerStates = this._layerStates.asReadonly();
  readonly urlLayerIds = this._urlLayerIds.asReadonly();
  readonly poiLayerStates = this._poiLayerStates.asReadonly();

  updatePosition(value: MapPosition | null): void {
    this._position.set(value);
  }

  updateMapMode(value: MapMode): void {
    this._mapMode.set(value);
  }

  updateResultMode(value: MapResultMode): void {
    this._resultMode.set(value);
  }

  updateLayerStates(value: MapLayerState[]): void {
    this._layerStates.set(value);
  }

  updateUrlLayerIds(value: string[]): void {
    this._urlLayerIds.set(value);
  }

  updatePoiLayerStates(value: MapLayerState[]): void {
    this._poiLayerStates.set(value);
  }
}
