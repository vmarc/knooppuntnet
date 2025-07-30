import { HttpResourceRef } from '@angular/common/http';
import { effect } from '@angular/core';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { MonitorRouteSegmentsPage } from '@api/common/monitor/monitor-route-segments-page';
import { SegmentInfo } from '@api/common/route/segment-info';
import { ApiResponse } from '@api/custom/api-response';
import { MapService } from '@app/map/map.service';
import { MonitorRouteService } from '@app/monitor/internal/route/monitor-route.service';
import { SegmentMap } from '@app/state/segment-map';
import { State } from '@app/state/state';
import { MonitorService } from '../../monitor.service';

@Injectable()
export class MonitorRouteSegmentsPageService {
  private readonly state = inject(State);
  private readonly monitorService = inject(MonitorService);
  private readonly monitorRouteService = inject(MonitorRouteService);
  private readonly mapService = inject(MapService);

  readonly response: HttpResourceRef<ApiResponse<MonitorRouteSegmentsPage>>;

  readonly monitorShowSegments = this.state.map.monitorShowSegments;

  constructor() {
    this.response = this.monitorRouteService.request('segments', (groupName, routeName) =>
      this.monitorService.routeSegments(groupName, routeName)
    );
    effect(() => {
      if (this.response.hasValue()) {
        const page = this.response.value().result;
        const summary = page.summary;
        if (summary) {
          this.monitorRouteService.update(summary);
          this.state.map.updateMode('route-segments');
          const segmentMap = SegmentMap.from(page.segments);
          this.state.map.updateSegmentMap(segmentMap);
          this.state.map.updateMonitorMode('segments');
          this.state.map.updateMonitorRouteIds([page.summary.routeId]);
          this.state.map.updateMonitorRelationIds(page.summary.relationIds);
          this.mapService.fitBounds(summary.bounds);
        }
      }
    });
  }

  selectSegment(segment: SegmentInfo): void {
    this.mapService.fitBounds(segment.bounds);
  }

  updateMonitorShowSegments(value: boolean): void {
    this.state.map.updateMonitorShowSegments(value);
  }
}
