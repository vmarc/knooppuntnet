import { inject } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { MapService } from '@app/map/map.service';
import { RouteDetailsService } from '@app/route/route-details-service';
import { NavService } from '@app/shared/components/nav.service';
import { State } from '@app/state/state';
import { MonitorService } from '../../monitor.service';
import { MonitorRouteDetailsPageState } from './monitor-route-details-page.state';
import { initialState } from './monitor-route-details-page.state';

@Injectable()
export class MonitorRouteDetailsPageService {
  private readonly state = inject(State);
  private readonly nav = inject(NavService);
  private readonly routeDetailsService = inject(RouteDetailsService);
  private readonly monitorService = inject(MonitorService);
  private readonly mapService = inject(MapService);

  private readonly _pageState = signal<MonitorRouteDetailsPageState>(initialState);
  readonly pageState = this._pageState.asReadonly();
  readonly admin = this.monitorService.admin;

  constructor() {
    const groupName = this.nav.param('groupName');
    const routeName = this.nav.param('routeName');
    const routeDescription = this.nav.state('description');
    this._pageState.update((state) => ({
      ...state,
      groupName,
      routeName,
      routeDescription,
    }));
    this.monitorService.route(groupName, routeName).subscribe((response) => {
      const page = response.result;
      const routeDescription = page?.routeDescription ?? this.pageState().routeDescription;
      this.routeDetailsService.update(groupName, routeName, response?.result?.referenceType);
      this._pageState.update((state) => ({
        ...state,
        routeDescription,
        response,
      }));
      if (page) {
        this.state.map.updateMode('monitor');
        this.state.map.updateMonitorRouteIds([page.routeId]);
        this.state.map.updateMonitorRelationIds(page.relationIds);
        this.mapService.fitBounds(page.bounds);
      }
    });
  }
}
