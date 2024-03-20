import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { RouteMapPage } from '@api/common/route';
import { ApiResponse } from '@api/custom';
import { MapPosition } from '@app/ol/domain';
import { ApiService } from '@app/services';
import { RouterService } from '../../../shared/services/router.service';
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
    this.routeService.initPage(this.routerService);
    this.load();
  }

  onAfterViewInit(): void {
    const mapPositionString = this.routerService.queryParam('position');
    const mapPositionFromUrl = MapPosition.fromQueryParam(mapPositionString);
    this.routeMapService.init(this.response().result.routeMapInfo, mapPositionFromUrl);
  }

  private load(): void {
    this.apiService.routeMap(this.routeService.routeId()).subscribe((response) => {
      if (response.result) {
        const networkType = response.result.routeMapInfo.networkType;
        const name = response.result.routeMapInfo.routeName;
        const changeCount = response.result.changeCount;
        this.routeService.updateRoute(networkType, name, changeCount);
      }
      this._response.set(response);
    });
  }
}
