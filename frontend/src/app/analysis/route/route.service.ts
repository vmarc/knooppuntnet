import { Location } from '@angular/common';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { NetworkType } from '@api/custom';
import { PreferencesService } from '@app/core';
import { RouterService } from '../../shared/services/router.service';

@Injectable({
  providedIn: 'root',
})
export class RouteService {
  private readonly _routeId = signal<string>(null);
  private readonly _routeName = signal<string>(null);
  private readonly _networkType = signal<NetworkType>(null);
  private readonly _changeCount = signal<number>(null);

  readonly routeId = this._routeId.asReadonly();
  readonly routeName = this._routeName.asReadonly();
  readonly networkType = this._networkType.asReadonly();
  readonly changeCount = this._changeCount.asReadonly();

  private location = inject(Location);
  private preferencesService = inject(PreferencesService);

  initPage(routerService: RouterService): void {
    const oldRouteId = this.routeId();
    const newRouteId = routerService.param('routeId');
    if (!oldRouteId || oldRouteId !== newRouteId) {
      this._routeId.set(newRouteId);
      this._changeCount.set(null);
      let newRouteName: string = undefined;
      let newNetworkType: NetworkType = undefined;
      const state = this.location.getState();
      if (state) {
        newRouteName = state['routeName'];
        newNetworkType = state['networkType'];
      }
      this._routeName.set(newRouteName);
      this._networkType.set(newNetworkType);
      if (newNetworkType) {
        this.preferencesService.setNetworkType(newNetworkType);
      }
    }
  }

  updateRoute(networkType: NetworkType, routeName: string, changeCount: number): void {
    this._routeName.set(routeName);
    this._networkType.set(networkType);
    this._changeCount.set(changeCount);
  }
}
