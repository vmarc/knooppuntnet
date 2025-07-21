import { inject } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { MapService } from '@app/map/map.service';
import { MonitorRouteMembersPageState } from './monitor-route-members-page.state';
import { initialState } from './monitor-route-members-page.state';
import { RouteDetailsService } from '@app/route/route-details-service';
import { NavService } from '@app/shared/components/nav.service';
import { State } from '@app/state/state';
import { MonitorService } from '../../monitor.service';

@Injectable()
export class MonitorRouteMembersPageService {
  private readonly state = inject(State);
  private readonly nav = inject(NavService);
  private readonly monitorService = inject(MonitorService);
  private readonly mapService = inject(MapService);
  private readonly routeDetailsService = inject(RouteDetailsService);

  private readonly _pageState = signal<MonitorRouteMembersPageState>(initialState);
  readonly pageState = this._pageState.asReadonly();
  readonly adminEnabled = this.monitorService.adminEnabled;

  constructor() {
    const groupName = this.nav.param('groupName');
    const routeName = this.nav.param('routeName');
    const routeDescription = this.nav.state('description');

    const summary = {
      adminUser: false,
      groupName: groupName,
      routeName: routeName,
      routeDescription: routeDescription,
      routeId: '',
      relationId: 0,
      relationIds: [],
      memberCount: 0,
      segmentCount: 0,
      deviationCount: 0,
      bounds: undefined,
    };

    this._pageState.update((state) => ({
      ...state,
      summary,
    }));
    this.monitorService.routeMembers(summary.groupName, summary.routeName).subscribe((response) => {
      const page = response.result;
      const summary = page?.summary ?? this.pageState().summary;
      this.routeDetailsService.update(
        summary.groupName,
        summary.routeName,
        response?.result?.referenceType
      );
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
