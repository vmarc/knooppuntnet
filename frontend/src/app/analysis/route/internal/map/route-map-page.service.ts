import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { RouteMapPage } from '@api/common/route/route-map-page';
import { ApiResponse } from '@api/custom/api-response';
import { MapPosition } from '@app/ol/domain/map-position';
import { ApiService } from '@app/shared/services/api.service';
import { RouterService } from '@app/shared/services/router.service';
import { RouteService } from '../route.service';
import { RouteMapService } from './components/route-map.service';

@Injectable()
export class RouteMapPageService {
  private readonly apiService = inject(ApiService);
  private readonly routeService = inject(RouteService);
  private readonly routeMapService = inject(RouteMapService);
  private readonly routerService = inject(RouterService);

  private readonly _response = signal<ApiResponse<RouteMapPage>>(null);
  readonly response = this._response.asReadonly();

  onInit(): void {
    this.routeService.onPage('map');
    this.load();
  }

  onAfterViewInit(): void {
    const mapPositionString = this.routerService.queryParam('position');
    const mapPositionFromUrl = MapPosition.fromQueryParam(mapPositionString);
    this.routeMapService.init(
      this.response().result,
      mapPositionFromUrl,
      this.routerService.urlLayerIds()
    );
  }

  private load(): void {
    this.apiService.routeMap(this.routeService.routeId()).subscribe((response) => {
      if (response.result) {
        this.routeService.updateRoute(response.result.routeInfo);
      }
      this._response.set(response);
    });
  }
}
