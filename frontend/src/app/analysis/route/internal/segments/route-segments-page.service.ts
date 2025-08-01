import { HttpResourceRef } from '@angular/common/http';
import { effect } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { RouteSegment } from '@api/common/route/route-segment';
import { RouteSegmentsPage } from '@api/common/route/route-segments-page';
import { SegmentInfo } from '@api/common/route/segment-info';
import { ApiResponse } from '@api/custom/api-response';
import { ApiService } from '@app/shared/services/api.service';
import { MapService } from '@app/map/map.service';
import { SegmentMap } from '@app/state/segment-map';
import { State } from '@app/state/state';
import { RouteService } from '../route.service';

@Injectable()
export class RouteSegmentsPageService {
  private readonly state = inject(State);
  private readonly apiService = inject(ApiService);
  private readonly routeService = inject(RouteService);
  private readonly mapService = inject(MapService);

  private readonly _selectedSegment = signal<RouteSegment>(null);
  readonly selectedSegment = this._selectedSegment.asReadonly();

  readonly response: HttpResourceRef<ApiResponse<RouteSegmentsPage>>;

  constructor() {
    this.response = this.routeService.request('segments', () =>
      this.apiService.routeSegments(this.routeService.routeId())
    );
    effect(() => {
      if (this.response.hasValue()) {
        const segments = this.response.value().result?.segments ?? [];
        const relationIds = segments.flatMap((segment) =>
          segment.routeInfos.map((routeInfo) => routeInfo.relationId)
        );
        const segmentMap = SegmentMap.from(segments);
        this.state.routeSegmentsPageOpened(segmentMap, this.routeService.routeId(), relationIds);
        this.mapService.fitBounds(this.routeService.bounds());
      }
    });
  }

  selectSegment(segment: SegmentInfo): void {
    this.mapService.fitBounds(segment.bounds);
  }
}
