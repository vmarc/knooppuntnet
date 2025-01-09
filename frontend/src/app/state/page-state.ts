import { signal } from '@angular/core';
import { RouteType } from '@api/common';

export class PageState {
  private readonly _small = signal<boolean>(false);
  private readonly _activePanel = signal<string>('map');
  private readonly _routeType = signal<RouteType>('hiking');

  readonly small = this._small.asReadonly();
  readonly activePanel = this._activePanel.asReadonly();
  readonly routeType = this._routeType.asReadonly();

  updateSmall(value: boolean): void {
    this._small.set(value);
  }

  updateActivePanel(value: string): void {
    this._activePanel.set(value);
  }

  updateRouteType(value: RouteType): void {
    this._routeType.set(value);
  }
}
