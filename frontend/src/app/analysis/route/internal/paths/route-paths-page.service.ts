import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RoutePathsPage } from '@api/common/route/route-paths-page';
import { ApiResponse } from '@api/custom/api-response';
import { ApiService } from '@app/shared/services/api.service';
import { Subscriptions } from '@app/util/subscriptions';
import { RouterService } from '@app/shared/services/router.service';
import { RouteService } from '../route.service';

@Injectable()
export class RoutePathsPageService {
  private readonly apiService = inject(ApiService);
  private readonly routeService = inject(RouteService);
  private readonly routerService = inject(RouterService);
  private readonly activatedRoute = inject(ActivatedRoute);
  private readonly subscriptions = new Subscriptions();

  private readonly _response = signal<ApiResponse<RoutePathsPage>>(null);
  readonly response = this._response.asReadonly();

  onInit(): void {
    this.subscriptions.add(
      this.activatedRoute.params.subscribe((params) => {
        this.routeService.initPage(this.routerService);
        this.load();
      })
    );
  }

  private load(): void {
    this.apiService.routePaths(this.routeService.routeId()).subscribe((response) => {
      if (response.result) {
        this.routeService.updateRoute(response.result.routeInfo);
      }
      this._response.set(response);
    });
  }
}
