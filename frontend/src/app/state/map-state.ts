import { computed } from '@angular/core';
import { signal } from '@angular/core';
import { Coordinate } from 'ol/coordinate';
import { MapRoutePopupState } from './map-route-popup-state';

export class MapState {
  private readonly _viewZoom = signal<number>(0);
  private readonly _center = signal<Coordinate | null>(null);
  private readonly _routePopupState = signal<MapRoutePopupState>(
    new MapRoutePopupState([], [0, 0])
  );
  private readonly _mode = signal<string>('explore');
  private readonly _selectedRoute = signal<number | null>(null);

  readonly zoom = computed(() => Math.floor(this._viewZoom()));
  readonly center = this._center.asReadonly();
  readonly routePopupState = this._routePopupState.asReadonly();
  readonly mode = this._mode.asReadonly();
  readonly selectedRoute = this._selectedRoute.asReadonly();

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
}
