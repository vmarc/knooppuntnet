import { inject } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { MapService } from '@app/map/map.service';
import { MonitorRouteService } from '@app/monitor/internal/route/monitor-route.service';
import { MonitorRouteSegmentsPageState } from './monitor-route-segments-page-state';
import { initialState } from './monitor-route-segments-page-state';
import { RouteDetailsService } from '@app/route/route-details-service';
import { NavService } from '@app/shared/components/nav.service';
import { State } from '@app/state/state';
import { MonitorService } from '../../monitor.service';

@Injectable()
export class MonitorRouteSegmentsPageService {
  private readonly state = inject(State);
  private readonly nav = inject(NavService);
  private readonly monitorService = inject(MonitorService);
  private readonly monitorRouteService = inject(MonitorRouteService);
  private readonly mapService = inject(MapService);
  private readonly routeDetailsService = inject(RouteDetailsService);

  private readonly _pageState = signal<MonitorRouteSegmentsPageState>(initialState);
  readonly pageState = this._pageState.asReadonly();

  constructor() {
    this.monitorRouteService.initPage(this.nav);
    const groupName = this.monitorRouteService.summary().groupName;
    const routeName = this.monitorRouteService.summary().routeName;
    this.monitorService.routeSegments(groupName, routeName).subscribe((response) => {
      const page = response.result;
      const summary = page?.summary ?? this.pageState().summary;
      if (summary) {
        this.monitorRouteService.update(summary);
      }
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
