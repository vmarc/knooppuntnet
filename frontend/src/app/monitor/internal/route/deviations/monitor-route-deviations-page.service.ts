import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { MonitorRouteDeviationInfo } from '@api/common/monitor/monitor-route-deviation-info';
import { MonitorRouteDeviationsPage } from '@api/common/monitor/monitor-route-deviations-page';
import { ApiResponse } from '@api/custom/api-response';
import { OldMapService } from '@app/mapold/old-map.service';
import { MonitorRouteService } from '@app/monitor/internal/route/monitor-route.service';
import { State } from '@app/state/state';
import { MonitorService } from '../../monitor.service';

@Injectable()
export class MonitorRouteDeviationsPageService {
  private readonly state = inject(State);
  private readonly monitorService = inject(MonitorService);
  private readonly monitorRouteService = inject(MonitorRouteService);
  private readonly mapService = inject(OldMapService);

  private readonly _response = signal<ApiResponse<MonitorRouteDeviationsPage>>(undefined);
  readonly response = this._response.asReadonly();

  constructor() {
    const groupName = this.monitorRouteService.summary().groupName;
    const routeName = this.monitorRouteService.summary().routeName;
    this.monitorService.routeDeviations(groupName, routeName).subscribe((response) => {
      this._response.set(response);
      const summary = response.result?.summary;
      if (summary) {
        this.monitorRouteService.update(summary);
        this.state.monitorPageOpened(summary.routeId, summary.relationIds);
        this.mapService.fitBounds(summary.bounds);
      }
    });
  }

  selectDeviation(deviation: MonitorRouteDeviationInfo) {
    this.mapService.fitBounds(deviation.bounds);
  }
}
