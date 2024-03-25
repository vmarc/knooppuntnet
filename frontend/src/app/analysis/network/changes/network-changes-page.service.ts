import { signal } from '@angular/core';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangesParameters } from '@api/common/changes/filter';
import { NetworkChangesPage } from '@api/common/network';
import { ApiResponse } from '@api/custom';
import { Util } from '@app/components/shared';
import { PreferencesService } from '@app/core';
import { ChangeOption } from '@app/kpn/common';
import { ApiService } from '@app/services';
import { PageParams } from '@app/shared/base';
import { RouterService } from '../../../shared/services/router.service';
import { UserService } from '../../../shared/user';
import { NetworkService } from '../network.service';

export class NetworkChangesPageService {
  private readonly apiService = inject(ApiService);
  private readonly networkService = inject(NetworkService);
  private readonly preferencesService = inject(PreferencesService);
  private readonly routerService = inject(RouterService);
  private readonly userService = inject(UserService);

  private readonly _response = signal<ApiResponse<NetworkChangesPage>>(null);
  private readonly _changesParameters = signal<ChangesParameters>(null);

  readonly loggedIn = this.userService.loggedIn;
  readonly response = this._response.asReadonly();
  readonly changesParameters = this._changesParameters.asReadonly();
  readonly impact = computed(() => this.changesParameters().impact);
  readonly pageSize = computed(() => this.changesParameters().pageSize);
  readonly pageIndex = computed(() => this.changesParameters().pageIndex);
  readonly filterOptions = computed(() => this.response()?.result?.filterOptions);

  onInit(): void {
    this.networkService.initPage(this.routerService);
    const params = this.routerService.params();
    const queryParams = this.routerService.queryParams();
    const uniqueQueryParams = Util.uniqueParams(queryParams);
    const pageParams = new PageParams(params, uniqueQueryParams);
    const preferencesImpact = this.preferencesService.impact();
    const preferencesPageSize = this.preferencesService.pageSize();
    const changesParameters = pageParams.changesParameters(preferencesImpact, preferencesPageSize);
    this._changesParameters.set(changesParameters);
    this.load();
  }

  setPageSize(pageSize: number): void {
    this.preferencesService.setPageSize(pageSize);
    this.setChangeParameters({
      ...this.changesParameters(),
      pageIndex: 0,
      pageSize,
    });
  }

  setImpact(impact: boolean): void {
    this.setChangeParameters({
      ...this.changesParameters(),
      pageIndex: 0,
      impact,
    });
  }

  setPageIndex(pageIndex: number): void {
    this.setChangeParameters({
      ...this.changesParameters(),
      pageIndex,
    });
  }

  setFilterOption(option: ChangeOption): void {
    this.setChangeParameters({
      ...this.changesParameters(),
      year: option.year,
      month: option.month,
      day: option.day,
      impact: option.impact,
      pageIndex: 0,
    });
  }

  private setChangeParameters(changeParameters: ChangesParameters): void {
    this._changesParameters.set(changeParameters);
    this.load();
  }

  private load(): void {
    this.apiService
      .networkChanges(this.networkService.networkId(), this.changesParameters())
      .subscribe((response) => {
        if (response.result) {
          this.networkService.setSummary(response.result.network);
        }
        this._response.set(response);
      });
  }
}
