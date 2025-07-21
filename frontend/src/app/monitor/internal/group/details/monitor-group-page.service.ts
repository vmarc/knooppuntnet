import { inject } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { MonitorRouteDetail } from '@api/common/monitor/monitor-route-detail';
import { MapService } from '@app/map/map.service';
import { NavService } from '@app/shared/components/nav.service';
import { State } from '@app/state/state';
import { MonitorService } from '../../monitor.service';
import { MonitorGroupPageState } from './monitor-group-page.state';
import { initialState } from './monitor-group-page.state';

@Injectable()
export class MonitorGroupPageService {
  private readonly state = inject(State);
  private readonly navService = inject(NavService);
  private readonly monitorService = inject(MonitorService);
  private readonly mapService = inject(MapService);

  private readonly _pageState = signal<MonitorGroupPageState>(initialState);
  readonly pageState = this._pageState.asReadonly();
  readonly adminEnabled = this.monitorService.adminEnabled;

  constructor() {
    const groupName = this.navService.param('groupName');
    const groupDescription = this.navService.state('description');
    this._pageState.update((state) => ({
      ...state,
      groupName,
      groupDescription,
    }));

    this.monitorService.group(groupName).subscribe((response) => {
      const page = response.result;
      const groupDescription = page?.groupDescription ?? this.pageState().groupDescription;
      this._pageState.update((state) => ({
        ...state,
        groupDescription,
        response,
      }));
      if (page) {
        const routeIds = page.routes.map((route) => route.routeId);
        this.state.map.updateMode('monitor');
        this.state.map.updateMonitorRouteIds(routeIds);
        this.state.map.updateMonitorRelationIds(page.relationIds);
        this.mapService.fitBounds(page.bounds);
      }
    });
  }

  selectRoute(route: MonitorRouteDetail) {
    this.state.map.updateMonitorRouteIds([route.routeId]);
    this.state.map.updateMonitorRelationIds(route.relationIds);
  }
}
