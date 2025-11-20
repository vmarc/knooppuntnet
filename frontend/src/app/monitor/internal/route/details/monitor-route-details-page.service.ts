import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { MonitorRouteDetailsPage } from '@api/common/monitor/monitor-route-details-page';
import { ApiResponse } from '@api/custom/api-response';
import { MapService } from '@app/map/map.service';
import { MonitorRouteService } from '@app/monitor/internal/route/monitor-route.service';
import { State } from '@app/state/state';
import { MonitorService } from '../../monitor.service';

@Injectable()
export class MonitorRouteDetailsPageService {
  private readonly state = inject(State);
  private readonly monitorService = inject(MonitorService);
  private readonly monitorRouteService = inject(MonitorRouteService);
  private readonly mapService = inject(MapService);

  private readonly _response = signal<ApiResponse<MonitorRouteDetailsPage>>(undefined);
  readonly response = this._response.asReadonly();

  readonly admin = this.monitorService.adminEnabled;

  constructor() {
    const groupName = this.monitorRouteService.summary().groupName;
    const routeName = this.monitorRouteService.summary().routeName;
    this.monitorService.route(groupName, routeName).subscribe((response) => {
      this._response.set(response);
      const page = response.result;
      const summary = page?.summary;
      if (summary) {
        this.monitorRouteService.update(summary);
        this.state.monitorPageOpened(summary.routeId, summary.relationIds);
        this.mapService.fitBounds(summary.bounds);
      }
    });
  }
}
