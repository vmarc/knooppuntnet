import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { RouteDetailsPage } from '@api/common/route';
import { ApiResponse } from '@api/custom';
import { ApiService } from '@app/services';
import { RouterService } from '../../../shared/services/router.service';
import { RouteService } from '../route.service';

@Injectable()
export class RouteDetailsPageService {
  private readonly apiService = inject(ApiService);
  private readonly routeService = inject(RouteService);
  private readonly routerService = inject(RouterService);

  private readonly _response = signal<ApiResponse<RouteDetailsPage>>(null);
  readonly response = this._response.asReadonly();

  onInit(): void {
    this.routeService.initPage(this.routerService);
    this.load();
  }

  private load(): void {
    this.apiService.routeDetails(this.routeService.routeId()).subscribe((response) => {
      if (response.result) {
        const name = response.result.route.summary.name;
        const networkType = response.result.route.summary.networkType;
        const changeCount = response.result.changeCount;
        this.routeService.updateRoute(networkType, name, changeCount);
      }
      this._response.set(response);
    });
  }
}
