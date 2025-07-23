import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { RouteSegment } from '@api/common/route/route-segment';
import { RouteSegmentsPage } from '@api/common/route/route-segments-page';
import { ApiResponse } from '@api/custom/api-response';
import { ApiService } from '@app/shared/services/api.service';
import { FocusElements } from '@app/state/focus-elements';
import { MapService } from '@app/map/map.service';
import { RouteService } from '../route.service';

@Injectable()
export class RouteSegmentsPageService {
  private readonly apiService = inject(ApiService);
  private readonly routeService = inject(RouteService);
  private readonly mapService = inject(MapService);

  private readonly _response = signal<ApiResponse<RouteSegmentsPage>>(null);
  readonly response = this._response.asReadonly();

  private readonly _selectedSegment = signal<RouteSegment>(null);
  readonly selectedSegment = this._selectedSegment.asReadonly();

  constructor() {
    this.routeService.onPage('segments');
    this.apiService.routeSegments(this.routeService.routeId()).subscribe((response) => {
      if (response.result) {
        this.routeService.updateRoute(response.result.routeInfo);
      }
      this._response.set(response);
    });
  }

  selectSegment(routeSegment: RouteSegment): void {
    this.mapService.setMapMode('route-segments');
    const elements: FocusElements = {
      nodeIds: [],
      routeIds: [],
    };
    if (routeSegment) {
      this.mapService.focusElements(routeSegment.bounds, elements);
    } else {
      this.mapService.focusElements(this.response().result.routeInfo.bounds, elements);
    }
    this._selectedSegment.set(routeSegment);
  }
}
