import { Location } from '@angular/common';
import { computed } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { RouteType } from '@api/common/route-type';
import { PreferencesService } from '@app/shared/core/preferences/preferences.service';
import { State } from '@app/state/state';
import { RouterService } from '@app/shared/services/router.service';

@Injectable({
  providedIn: 'root',
})
export class RouteService {
  private readonly state = inject(State);
  private readonly _routeId = signal<string>(null);
  private readonly _routeName = signal<string>(null);
  private readonly _routeType = signal<RouteType>(null);
  private readonly _changeCount = signal<number>(null);

  readonly routeId = this._routeId.asReadonly();
  readonly routeName = this._routeName.asReadonly();
  readonly routeType = this._routeType.asReadonly();
  readonly changeCount = this._changeCount.asReadonly();
  readonly routeDisplayName = computed(() => this.routeName() || this.routeId());

  private location = inject(Location);
  private preferencesService = inject(PreferencesService);

  initPage(routerService: RouterService): void {
    const oldRouteId = this.routeId();
    const newRouteId = routerService.param('routeId');
    if (!oldRouteId || oldRouteId !== newRouteId) {
      this._routeId.set(newRouteId);
      this._changeCount.set(null);
      let newRouteName: string = undefined;
      let newRouteType: RouteType = undefined;
      const state = this.location.getState();
      if (state) {
        newRouteName = state['routeName'];
        newRouteType = state['routeType'];
      }
      this._routeName.set(newRouteName);
      this._routeType.set(newRouteType);
      if (newRouteType) {
        this.state.preferences.updateRouteType(newRouteType);
      }
    }
  }

  updateRoute(routeType: RouteType, routeName: string, changeCount: number): void {
    this._routeName.set(routeName);
    this._routeType.set(routeType);
    this._changeCount.set(changeCount);
  }
}
