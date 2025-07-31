import { HttpResourceRef } from '@angular/common/http';
import { effect } from '@angular/core';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { MonitorRouteDetailsPage } from '@api/common/monitor/monitor-route-details-page';
import { ApiResponse } from '@api/custom/api-response';
import { MapService } from '@app/map/map.service';
import { MonitorRouteService } from '@app/monitor/internal/route/monitor-route.service';
import { FocusElements } from '@app/state/focus-elements';
import { State } from '@app/state/state';
import { MonitorService } from '../../monitor.service';

@Injectable()
export class MonitorRouteDetailsPageService {
  private readonly state = inject(State);
  private readonly monitorService = inject(MonitorService);
  private readonly monitorRouteService = inject(MonitorRouteService);
  private readonly mapService = inject(MapService);

  readonly response: HttpResourceRef<ApiResponse<MonitorRouteDetailsPage>>;

  readonly admin = this.monitorService.adminEnabled;

  constructor() {
    this.response = this.monitorRouteService.request('details', (groupName, routeName) =>
      this.monitorService.route(groupName, routeName)
    );
    effect(() => {
      if (this.response.hasValue()) {
        const summary = this.response.value().result.summary;
        if (summary) {
          this.monitorRouteService.update(summary);
          this.state.monitorPageOpened(summary.routeId, summary.relationIds);
          this.mapService.fitBounds(summary.bounds);
        }
      }
    });
  }
}
