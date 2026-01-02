import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { RouteMembersPage } from '@api/common/route/route-members-page';
import { ApiResponse } from '@api/custom/api-response';
import { ApiService } from '@app/shared/services/api.service';
import { RouterService } from '@app/shared/services/router.service';
import { RouteService } from '../route.service';

@Injectable()
export class RouteMembersPageService {
  private readonly apiService = inject(ApiService);
  private readonly routeService = inject(RouteService);
  private readonly routerService = inject(RouterService);

  private readonly _response = signal<ApiResponse<RouteMembersPage>>(null);
  readonly response = this._response.asReadonly();

  onInit(): void {
    const routeId = +this.routerService.params()['routeId'];
    this.routeService.onInit('members', routeId);
    this.apiService.routeMembers(routeId).subscribe((response) => {
      this._response.set(response);
      if (response.result?.routeInfo) {
        this.routeService.updateRoute(response.result.routeInfo);
      } else {
        this.routeService.updateRouteNotFound(true);
      }
    });
  }
}
