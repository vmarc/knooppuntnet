import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { RoutePathsPage } from '@api/common/route/route-paths-page';
import { ApiResponse } from '@api/custom/api-response';
import { MapService } from '@app/map/map.service';
import { RouteSourceIds } from '@app/map/sources/route-source-ids';
import { ApiService } from '@app/shared/services/api.service';
import { RouterService } from '@app/shared/services/router.service';
import { RouteService } from '../route.service';

@Injectable()
export class RoutePathsPageService {
  private readonly apiService = inject(ApiService);
  private readonly routeService = inject(RouteService);
  private readonly routerService = inject(RouterService);
  private readonly mapService = inject(MapService);

  private readonly _response = signal<ApiResponse<RoutePathsPage>>(null);
  readonly response = this._response.asReadonly();

  onInit(): void {
    const routeId = +this.routerService.param('routeId');
    this.routeService.onInit('paths', routeId);
    this.apiService.routePaths(routeId).subscribe((response) => {
      this._response.set(response);
      if (response.result?.routeInfo) {
        this.routeService.updateRoute(response.result.routeInfo);
        this.mapService.execute(() => {
          this.mapService.hideLayer(new RouteSourceIds('hiking').nodeRouteLayerId());
          this.mapService.hideLayer(new RouteSourceIds('hiking').nodeRouteSegmentLayerId());
          this.mapService.showLayer(new RouteSourceIds('hiking').nodeRoutePathLayerId());
          this.mapService.fitBounds(this.routeService.bounds());
        });
      } else {
        this.routeService.updateRouteNotFound(true);
      }
    });
  }

  onDestroy(): void {
    this.mapService.hideLayer(new RouteSourceIds('hiking').nodeRoutePathLayerId());
    this.mapService.showLayer(new RouteSourceIds('hiking').nodeRouteLayerId());
  }
}
