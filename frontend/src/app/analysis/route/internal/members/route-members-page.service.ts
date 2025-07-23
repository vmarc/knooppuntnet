import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { RouteMembersPage } from '@api/common/route/route-members-page';
import { ApiResponse } from '@api/custom/api-response';
import { ApiService } from '@app/shared/services/api.service';
import { RouteService } from '../route.service';

@Injectable()
export class RouteMembersPageService {
  private readonly apiService = inject(ApiService);
  private readonly routeService = inject(RouteService);

  private readonly _response = signal<ApiResponse<RouteMembersPage>>(null);
  readonly response = this._response.asReadonly();

  constructor() {
    this.routeService.onPage('members');
    this.apiService.routeMembers(this.routeService.routeId()).subscribe((response) => {
      if (response.result) {
        this.routeService.updateRoute(response.result.routeInfo);
      }
      this._response.set(response);
    });
  }
}
