import { inject } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { MapService } from '@app/map/map.service';
import { MonitorRouteService } from '@app/monitor/internal/route/monitor-route.service';
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
  private readonly monitorRouteService = inject(MonitorRouteService);
  private readonly mapService = inject(MapService);

  private readonly _pageState = signal<MonitorRouteDetailsPageState>(initialState);
  readonly pageState = this._pageState.asReadonly();
  readonly admin = this.monitorService.adminEnabled;

  constructor() {
    this.monitorRouteService.initPage(this.nav);
    const groupName = this.monitorRouteService.summary().groupName;
    const routeName = this.monitorRouteService.summary().routeName;
    this.monitorService.route(groupName, routeName).subscribe((response) => {
      const page = response.result;
      const summary = page?.summary ?? this.pageState().summary;
      if (summary) {
        this.monitorRouteService.update(summary);
      }
      this.routeDetailsService.update(groupName, routeName, response?.result?.referenceType);
      this._pageState.update((state) => ({
        ...state,
        summary,
        response,
      }));
      if (page) {
        this.state.map.updateMode('monitor');
        this.state.map.updateMonitorRouteIds([page.summary.routeId]);
        this.state.map.updateMonitorRelationIds(page.summary.relationIds);
        this.mapService.fitBounds(page.summary.bounds);
      }
    });
  }
}
