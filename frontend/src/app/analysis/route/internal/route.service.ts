import { Location } from '@angular/common';
import { computed } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { RouteType } from '@api/common/route-type';
import { RouteInfo } from '@api/common/route/route-info';
import { State } from '@app/state/state';
import { RouterService } from '@app/shared/services/router.service';

@Injectable({
  providedIn: 'root',
})
export class RouteService {
  private readonly state = inject(State);

  private readonly _routeInfo = signal<RouteInfo>(null);
  readonly routeId = computed(() => this._routeInfo()?.routeId);
  readonly routeName = computed(() => this._routeInfo()?.routeName);
  readonly routeTypes = computed(() => this._routeInfo()?.routeTypes);
  readonly changeCount = computed(() => this._routeInfo()?.changeCount);
  readonly routeDisplayName = computed(() => this.routeName() || '' + this.routeId());

  private location = inject(Location);

  initPage(routerService: RouterService): void {
    const oldRouteId = '' + this.routeId();
    const newRouteId = routerService.param('routeId');
    if (!oldRouteId || oldRouteId !== newRouteId) {
      let newRouteName: string = undefined;
      let newRouteType: RouteType = undefined;
      const state = this.location.getState();
      if (state) {
        newRouteName = state['routeName'];
        newRouteType = state['routeType'];
      }
      const routeInfo: RouteInfo = {
        routeId: +newRouteId,
        routeName: newRouteName,
        routeTypes: [newRouteType],
        changeCount: 0,
        segmentCount: 0,
      };

      this._routeInfo.set(routeInfo);
      if (newRouteType) {
        this.state.preferences.updateRouteType(newRouteType);
      }
    }
  }

  updateRoute(routeInfo: RouteInfo): void {
    this._routeInfo.set(routeInfo);
  }
}
