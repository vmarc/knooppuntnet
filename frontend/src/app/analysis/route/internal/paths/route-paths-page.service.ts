import { HttpResourceRef } from '@angular/common/http';
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

  readonly response: HttpResourceRef<ApiResponse<RoutePathsPage>>;

  constructor() {
    this.response = this.routeService.request('paths', () =>
      this.apiService.routePaths(this.routeService.routeId())
    );
  }
}
