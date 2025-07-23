import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { RoutePathsPage } from '@api/common/route/route-paths-page';
import { ApiResponse } from '@api/custom/api-response';
import { ApiService } from '@app/shared/services/api.service';
import { RouteService } from '../route.service';

@Injectable()
export class RoutePathsPageService {
  private readonly apiService = inject(ApiService);
  private readonly routeService = inject(RouteService);

  private readonly _response = signal<ApiResponse<RoutePathsPage>>(null);
  readonly response = this._response.asReadonly();

  constructor() {
    this.routeService.onPage('paths');
    this.apiService.routePaths(this.routeService.routeId()).subscribe((response) => {
      if (response.result) {
        this.routeService.updateRoute(response.result.routeInfo);
      }
      this._response.set(response);
    });
  }
}
