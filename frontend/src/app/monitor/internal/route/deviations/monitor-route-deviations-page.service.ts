import { HttpResourceRef } from '@angular/common/http';
import { effect } from '@angular/core';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { MonitorRouteDeviationInfo } from '@api/common/monitor/monitor-route-deviation-info';
import { MonitorRouteDeviationsPage } from '@api/common/monitor/monitor-route-deviations-page';
import { ApiResponse } from '@api/custom/api-response';
import { MapService } from '@app/map/map.service';
import { MonitorRouteService } from '@app/monitor/internal/route/monitor-route.service';
import { NavService } from '@app/shared/components/nav.service';
import { State } from '@app/state/state';
import { MonitorService } from '../../monitor.service';

@Injectable()
export class MonitorRouteDeviationsPageService {
  private readonly state = inject(State);
  private readonly nav = inject(NavService);
  private readonly monitorService = inject(MonitorService);
  private readonly monitorRouteService = inject(MonitorRouteService);
  private readonly mapService = inject(MapService);

  readonly response: HttpResourceRef<ApiResponse<MonitorRouteDeviationsPage>>;

  constructor() {
    this.monitorRouteService.initPage(this.nav);
    const groupName = this.monitorRouteService.summary().groupName;
    const routeName = this.monitorRouteService.summary().routeName;
    this.response = this.monitorService.routeDeviations(groupName, routeName);
    effect(() => {
      if (this.response.hasValue()) {
        const summary = this.response.value().result.summary;
        if (summary) {
          this.monitorRouteService.update(summary);
          this.state.map.updateMode('monitor');
          this.state.map.updateMonitorRouteIds([summary.routeId]);
          this.state.map.updateMonitorRelationIds(summary.relationIds);
          this.mapService.fitBounds(summary.bounds);
        }
      }
    });
  }

  selectDeviation(deviation: MonitorRouteDeviationInfo) {
    this.mapService.fitBounds(deviation.bounds);
  }
}
