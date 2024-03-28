import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { computed } from '@angular/core';
import { ChangesParameters } from '@api/common/changes/filter';
import { RouteChangesPage } from '@api/common/route';
import { ApiResponse } from '@api/custom';
import { Util } from '@app/components/shared';
import { PreferencesService } from '@app/core';
import { ChangeOption } from '@app/kpn/common';
import { ApiService } from '@app/services';
import { PageParams } from '@app/shared/base';
import { RouterService } from '../../../shared/services/router.service';
import { UserService } from '../../../shared/user';
import { RouteService } from '../route.service';

@Injectable()
export class RouteChangesPageService {
  private readonly apiService = inject(ApiService);
  private readonly routeService = inject(RouteService);
  private readonly routerService = inject(RouterService);
  private readonly preferencesService = inject(PreferencesService);
  private readonly userService = inject(UserService);

  readonly loggedIn = this.userService.loggedIn;

  private readonly _response = signal<ApiResponse<RouteChangesPage>>(null);
  readonly response = this._response.asReadonly();

  private readonly _changesParameters = signal<ChangesParameters>(null);
  readonly changesParameters = this._changesParameters.asReadonly();

  readonly impact = this.preferencesService.impact;
  readonly pageSize = this.preferencesService.pageSize;
  readonly pageIndex = computed(() => this.changesParameters()?.pageIndex);
  readonly filterOptions = computed(() => this.response()?.result?.filterOptions);

  onInit(): void {
    this.routeService.initPage(this.routerService);
    const params = this.routerService.params();
    const queryParams = this.routerService.queryParams();
    const uniqueQueryParams = Util.uniqueParams(queryParams);
    const pageParams = new PageParams(params, uniqueQueryParams);
    const preferencesImpact = this.preferencesService.impact();
    const preferencesPageSize = this.preferencesService.pageSize();
    const changesParameters = pageParams.changesParameters(preferencesImpact, preferencesPageSize);
    this._changesParameters.set(changesParameters);
    // TODO SIGNAL update query params (effect() on changesParameters?)
    this.load();
  }

  updatePageSize(pageSize: number): void {
    this.preferencesService.setPageSize(pageSize);
    this._changesParameters.set({
      ...this.changesParameters(),
      pageIndex: 0,
      pageSize,
    });
    // TODO SIGNAL update query params
    this.load();
  }

  updateImpact(impact: boolean): void {
    this.preferencesService.setImpact(impact);
    this._changesParameters.set({
      ...this.changesParameters(),
      pageIndex: 0,
      impact,
    });
    // TODO SIGNAL update query params
    this.load();
  }

  updatePageIndex(pageIndex: number): void {
    this._changesParameters.set({
      ...this.changesParameters(),
      pageIndex,
    });
    // TODO SIGNAL update query params
    this.load();
  }

  updateFilterOption(option: ChangeOption): void {
    this._changesParameters.set({
      ...this.changesParameters(),
      year: option.year,
      month: option.month,
      day: option.day,
      impact: option.impact,
      pageIndex: 0,
    });
    // TODO SIGNAL update query params
    this.load();
  }

  private load(): void {
    this.apiService
      .routeChanges(this.routeService.routeId(), this.changesParameters())
      .subscribe((response) => {
        if (response.result) {
          const name = response.result.routeNameInfo.routeName;
          const networkType = response.result.routeNameInfo.networkType;
          const changeCount = response.result.changeCount;
          this.routeService.updateRoute(networkType, name, changeCount);
        }
        this._response.set(response);
      });
  }
}