import { HttpResourceRef } from '@angular/common/http';
import { effect } from '@angular/core';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { MonitorRouteMembersPage } from '@api/common/monitor/monitor-route-members-page';
import { ApiResponse } from '@api/custom/api-response';
import { MapService } from '@app/map/map.service';
import { MonitorRouteService } from '@app/monitor/internal/route/monitor-route.service';
import { State } from '@app/state/state';
import { MonitorService } from '../../monitor.service';

@Injectable()
export class MonitorRouteMembersPageService {
  private readonly state = inject(State);
  private readonly monitorService = inject(MonitorService);
  private readonly monitorRouteService = inject(MonitorRouteService);
  private readonly mapService = inject(MapService);

  readonly response: HttpResourceRef<ApiResponse<MonitorRouteMembersPage>>;

  constructor() {
    this.response = this.monitorRouteService.request('members', (groupName, routeName) =>
      this.monitorService.routeMembers(groupName, routeName)
    );
    effect(() => {
      if (this.response.hasValue()) {
        const summary = this.response.value().result.summary;
        if (summary) {
          this.monitorRouteService.update(summary);
          this.state.map.updateMode('monitor');
          this.state.map.updateMonitorMode('route');
          this.state.map.updateMonitorRouteIds([summary.routeId]);
          this.state.map.updateMonitorRelationIds(summary.relationIds);
          this.mapService.fitBounds(summary.bounds);
        }
      }
    });
  }
}
