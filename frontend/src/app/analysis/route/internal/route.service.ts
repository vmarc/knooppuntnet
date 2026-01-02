import { Location } from '@angular/common';
import { computed } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { RouteType } from '@api/common/route-type';
import { RouteInfo } from '@api/common/route/route-info';
import { RoutePageName } from '@app/analysis/route/internal/components/route-page-name';
import { State } from '@app/state/state';

@Injectable({
  providedIn: 'root',
})
export class RouteService {
  private readonly state = inject(State);

  private readonly _routeNotFound = signal<boolean>(false);
  readonly routeNotFound = this._routeNotFound.asReadonly();

  private readonly _routeInfo = signal<RouteInfo>(null);
  readonly routeId = computed(() => this._routeInfo()?.routeId);
  readonly routeName = computed(() => this._routeInfo()?.routeName);
  readonly routeTypes = computed(() => this._routeInfo()?.routeTypes);

  readonly memberCount = computed(() => this._routeInfo()?.memberCount);
  readonly pathCount = computed(() => this._routeInfo()?.pathCount);
  readonly segmentCount = computed(() => this._routeInfo()?.segmentCount);
  readonly changeCount = computed(() => this._routeInfo()?.changeCount);

  readonly routeDisplayName = computed(() => this.routeName() || '' + this.routeId());
  readonly bounds = computed(() => this._routeInfo()?.bounds);

  private readonly _pageName = signal<RoutePageName>(undefined);
  readonly pageName = this._pageName.asReadonly();

  private location = inject(Location);

  onInit(pageName: RoutePageName, newRouteId: number): void {
    this._pageName.set(pageName);
    this.updateRouteNotFound(false);
    const oldRouteId = this.routeId();
    if (!oldRouteId || oldRouteId !== newRouteId) {
      let newRouteName: string = undefined;
      let newRouteType: RouteType = undefined;
      const state = this.location.getState();
      if (state) {
        newRouteName = state['routeName'];
        newRouteType = state['routeType'];
      }
      const routeInfo: RouteInfo = {
        routeId: newRouteId,
        routeName: newRouteName,
        routeTypes: [newRouteType],
        memberCount: 0,
        pathCount: 0,
        segmentCount: 0,
        changeCount: 0,
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

  updateRouteNotFound(value: boolean): void {
    this._routeNotFound.set(value);
  }
}
