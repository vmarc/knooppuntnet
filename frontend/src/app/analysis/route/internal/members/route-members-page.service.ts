import { HttpResourceRef } from '@angular/common/http';
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

  readonly response: HttpResourceRef<ApiResponse<RouteMembersPage>>;

  constructor() {
    this.response = this.routeService.request('members', () =>
      this.apiService.routeMembers(this.routeService.routeId())
    );
  }
}
