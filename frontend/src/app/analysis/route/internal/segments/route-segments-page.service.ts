import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RouteSegment } from '@api/common/route/route-segment';
import { RouteSegmentsPage } from '@api/common/route/route-segments-page';
import { ApiResponse } from '@api/custom/api-response';
import { ApiService } from '@app/shared/services/api.service';
import { Subscriptions } from '@app/util/subscriptions';
import { FocusElements } from '@app/state/focus-elements';
import { MapService } from '@app/map/map.service';
import { RouterService } from '@app/shared/services/router.service';
import { RouteService } from '../route.service';

@Injectable()
export class RouteSegmentsPageService {
  private readonly apiService = inject(ApiService);
  private readonly mapService = inject(MapService);
  private readonly routeService = inject(RouteService);
  private readonly routerService = inject(RouterService);
  private readonly activatedRoute = inject(ActivatedRoute);
  private readonly subscriptions = new Subscriptions();

  private readonly _response = signal<ApiResponse<RouteSegmentsPage>>(null);
  readonly response = this._response.asReadonly();

  private readonly _selectedSegment = signal<RouteSegment>(null);
  readonly selectedSegment = this._selectedSegment.asReadonly();

  private readonly colors = [
    '#e6194B', // red
    '#3cb44b', // green
    '#ffe119', // yellow
    '#4363d8', // blue
    '#f58231', // orange
    '#911eb4', // purple
    '#42d4f4', // cyan
    '#f032e6', // magenta
    '#bfef45', // lime
    '#fabed4', // pink
    '#469990', // teal
    '#dcbeff', // lavender
    '#9A6324', // brown
    '#fffac8', // beige
    '#800000', // maroon
    '#aaffc3', // mint
    '#808000', // olive
    '#ffd8b1', // apricot
    '#000075', // navy
  ];

  onInit(): void {
    this.subscriptions.add(
      this.activatedRoute.params.subscribe((params) => {
        this.routeService.initPage(this.routerService);
        this.load();
      })
    );
  }

  private load(): void {
    this.apiService.routeSegments(this.routeService.routeId()).subscribe((response) => {
      if (response.result) {
        this.routeService.updateRoute(response.result.routeInfo);
      }
      this._response.set(response);
    });
  }

  colorForSegmentId(id: number): string {
    const index = (id - 1) % this.colors.length;
    return this.colors[index];
  }

  selectSegment(routeSegment: RouteSegment): void {
    const elements: FocusElements = {
      nodeIds: [],
      routeIds: [],
    };
    this.mapService.focusElements(routeSegment.bounds, elements);
    this._selectedSegment.set(routeSegment);
  }
}
