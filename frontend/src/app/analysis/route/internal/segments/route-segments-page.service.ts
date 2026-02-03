import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { RouteSegment } from '@api/common/route/route-segment';
import { RouteSegmentsPage } from '@api/common/route/route-segments-page';
import { SegmentInfo } from '@api/common/route/segment-info';
import { ApiResponse } from '@api/custom/api-response';
import { MapService } from '@app/map/map.service';
import { RouteSourceIds } from '@app/map/sources/route-source-ids';
import { ApiService } from '@app/shared/services/api.service';
import { RouterService } from '@app/shared/services/router.service';
import { SegmentMap } from '@app/state/segment-map';
import { State } from '@app/state/state';
import { RouteService } from '../route.service';

@Injectable()
export class RouteSegmentsPageService {
  private readonly state = inject(State);
  private readonly apiService = inject(ApiService);
  private readonly routeService = inject(RouteService);
  private readonly routerService = inject(RouterService);
  private readonly mapService = inject(MapService);

  private readonly _selectedSegment = signal<RouteSegment>(null);
  readonly selectedSegment = this._selectedSegment.asReadonly();

  private readonly _response = signal<ApiResponse<RouteSegmentsPage>>(null);
  readonly response = this._response.asReadonly();

  onInit() {
    const routeId = +this.routerService.param('routeId');
    this.routeService.onInit('segments', routeId);
    this.apiService.routeSegments(this.routeService.routeId()).subscribe((response) => {
      if (response.result?.routeInfo) {
        this.routeService.updateRoute(response.result.routeInfo);
      } else {
        this.routeService.updateRouteNotFound(true);
      }

      this._response.set(response);
      const segments = this.response().result?.segments ?? [];
      const relationIds = segments.flatMap((segment) =>
        segment.routeInfos.map((routeInfo) => routeInfo.relationId)
      );
      const segmentMap = SegmentMap.from(segments);
      this.state.routeSegmentsPageOpened(segmentMap, this.routeService.routeId(), relationIds);

      this.mapService.execute(() => {
        this.mapService.hideLayer(new RouteSourceIds('hiking').nodeRouteLayerId());
        this.mapService.showLayer(new RouteSourceIds('hiking').nodeRouteSegmentLayerId());
        this.mapService.fitBounds(this.routeService.bounds());
      });
    });
  }

  onDestroy(): void {
    this.mapService.hideLayer(new RouteSourceIds('hiking').nodeRouteSegmentLayerId());
    this.mapService.showLayer(new RouteSourceIds('hiking').nodeRouteLayerId());
  }
  selectSegment(segment: SegmentInfo): void {
    this.mapService.fitBounds(segment.bounds);
  }
}
