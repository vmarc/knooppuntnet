import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { computed } from '@angular/core';
import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
import { RouteChangesPage } from '@api/common/route/route-changes-page';
import { ApiResponse } from '@api/custom/api-response';
import { ChangesService } from '@app/analysis/components/changes/changes.service';
import { ChangeOption } from '@app/shared/kpn/common/change-option';
import { ApiService } from '@app/shared/services/api.service';
import { PageParams } from '@app/shared/base/page-params';
import { Util } from '@app/shared/components/util';
import { State } from '@app/state/state';
import { RouterService } from '@app/shared/services/router.service';
import { UserService } from '@app/shared/user/user.service';
import { RouteService } from '../route.service';

@Injectable()
export class RouteChangesPageService implements ChangesService {
  private readonly state = inject(State);
  private readonly apiService = inject(ApiService);
  private readonly routeService = inject(RouteService);
  private readonly routerService = inject(RouterService);
  private readonly userService = inject(UserService);

  readonly loggedIn = this.userService.loggedIn;

  private readonly _response = signal<ApiResponse<RouteChangesPage>>(null);
  readonly response = this._response.asReadonly();

  private readonly _changesParameters = signal<ChangesParameters>(null);
  readonly changesParameters = this._changesParameters.asReadonly();

  readonly situationOn = computed(() => this.response().situationOn);

  readonly impact = computed(() => this.changesParameters().impact);
  readonly pageSize = computed(() => this.changesParameters().pageSize);
  readonly pageIndex = computed(() => this.changesParameters().pageIndex);
  readonly filterOptions = computed(() => this.response()?.result?.filterOptions);
  readonly changeCount = this.routeService.changeCount;

  onInit(): void {
    this.routeService.initPage(this.routerService);
    const params = this.routerService.params();
    const queryParams = this.routerService.queryParams();
    const uniqueQueryParams = Util.uniqueParams(queryParams);
    const pageParams = new PageParams(params, uniqueQueryParams);
    const preferencesImpact = this.state.preferences.impact();
    const preferencesPageSize = this.state.preferences.pageSize();
    const changesParameters = pageParams.changesParameters(preferencesImpact, preferencesPageSize);
    this._changesParameters.set(changesParameters);
    this.load();
  }

  updatePageSize(pageSize: number): void {
    this.state.preferences.updatePageSize(pageSize);
    this._changesParameters.set({
      ...this.changesParameters(),
      pageIndex: 0,
      pageSize,
    });
    this.load();
  }

  updateImpact(impact: boolean): void {
    this.state.preferences.updateImpact(impact);
    this._changesParameters.set({
      ...this.changesParameters(),
      pageIndex: 0,
      impact,
    });
    this.load();
  }

  updatePageIndex(pageIndex: number): void {
    this._changesParameters.set({
      ...this.changesParameters(),
      pageIndex,
    });
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
    this.load();
  }

  private load(): void {
    this.routerService.updateQueryParams(this.changesParameters()).then(() => {
      this.apiService
        .routeChanges(this.routeService.routeId(), this.changesParameters())
        .subscribe((response) => {
          if (response.result) {
            const name = response.result.routeNameInfo.routeName;
            const routeType = response.result.routeNameInfo.routeType;
            const changeCount = response.result.changeCount;
            this.routeService.updateRoute(routeType, name, changeCount);
          }
          this._response.set(response);
        });
    });
  }
}
