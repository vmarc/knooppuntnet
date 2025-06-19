import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RoutePath } from '@api/common/route/route-path';
import { RoutePathsPage } from '@api/common/route/route-paths-page';
import { RouteSegment } from '@api/common/route/route-segment';
import { ApiResponse } from '@api/custom/api-response';
import { ApiService } from '@app/shared/services/api.service';
import { State } from '@app/state/state';
import { Subscriptions } from '@app/util/subscriptions';
import { MapService } from '@app/map/map.service';
import { RouterService } from '@app/shared/services/router.service';
import { RouteService } from '../route.service';

@Injectable()
export class RoutePathsPageService {
  private readonly state = inject(State);
  private readonly apiService = inject(ApiService);
  private readonly mapService = inject(MapService);
  private readonly routeService = inject(RouteService);
  private readonly routerService = inject(RouterService);
  private readonly activatedRoute = inject(ActivatedRoute);
  private readonly subscriptions = new Subscriptions();

  private readonly _response = signal<ApiResponse<RoutePathsPage>>(null);
  readonly response = this._response.asReadonly();

  private readonly _selectedSegment = signal<RouteSegment>(null);
  readonly selectedSegment = this._selectedSegment.asReadonly();
  onInit(): void {
    this.mapService.setMapMode('route-segments');
    this.subscriptions.add(
      this.activatedRoute.params.subscribe((params) => {
        this.routeService.initPage(this.routerService);
        this.load();
      })
    );
  }

  private load(): void {
    const routeId = this.routeService.routeId();
    this.state.map.updateSelectedRoute(routeId);
    this.apiService.routePaths(routeId).subscribe((response) => {
      if (response.result) {
        this.routeService.updateRoute(response.result.routeInfo);
      }
      this._response.set(response);
    });
  }

  selectSegment(routePath: RoutePath): void {
    // const elements: FocusElements = {
    //   nodeIds: [],
    //   routeIds: [],
    // };
    // if (routePath) {
    //   this.mapService.focusElements(routePath.bounds, elements);
    // } else {
    //   this.mapService.focusElements(this.response().result.routeInfo.bounds, elements);
    // }
    // this._selectedSegment.set(routePath);
  }
}
