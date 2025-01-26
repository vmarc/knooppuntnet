import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { signal } from '@angular/core';
import { ChangesPage } from '@api/common';
import { ChangesParameters } from '@api/common/changes/filter';
import { ApiResponse } from '@api/custom';
import { AnalysisStrategyService } from '@app/analysis/strategy';
import { Util } from '@app/components/shared';
import { ChangeOption } from '@app/kpn/common';
import { ApiService } from '@app/services';
import { PageParams } from '@app/shared/base';
import { State } from '@app/state';
import { RouterService } from '../../shared/services/router.service';
import { UserService } from '../../shared/user';

export class ChangesPageService {
  private readonly state = inject(State);
  private readonly apiService = inject(ApiService);
  private readonly routerService = inject(RouterService);
  private readonly analysisStrategyService = inject(AnalysisStrategyService);
  private readonly userService = inject(UserService);

  private readonly _changesParameters = signal<ChangesParameters | null>(null);
  private readonly _response = signal<ApiResponse<ChangesPage>>(null);

  readonly loggedIn = this.userService.loggedIn;
  readonly impact = computed(() => this.changesParameters().impact);
  readonly pageSize = computed(() => this.changesParameters().pageSize);
  readonly pageIndex = computed(() => this.changesParameters().pageIndex);
  readonly filterOptions = computed(() => this.response()?.result?.filterOptions);

  readonly changesParameters = this._changesParameters.asReadonly();
  readonly response = this._response.asReadonly();

  onInit(): void {
    this.analysisStrategyService.init();
    const uniqueQueryParams = Util.uniqueParams(this.routerService.queryParams());
    const pageParams = new PageParams(this.routerService.params(), uniqueQueryParams);
    const changesParameters = pageParams.changesParameters(
      this.state.preferences.impact(),
      this.state.preferences.pageSize()
    );
    this._changesParameters.set(changesParameters);
    this.load();
  }

  updatePageSize(pageSize: number): void {
    this.state.preferences.updatePageSize(pageSize);
    this.setChangeParameters({
      ...this.changesParameters(),
      pageIndex: 0,
      pageSize,
    });
  }

  updateImpact(impact: boolean): void {
    this.setChangeParameters({
      ...this.changesParameters(),
      pageIndex: 0,
      impact,
    });
  }

  updatePageIndex(pageIndex: number): void {
    this.setChangeParameters({
      ...this.changesParameters(),
      pageIndex,
    });
  }

  updateFilterOption(option: ChangeOption): void {
    this.setChangeParameters({
      ...this.changesParameters(),
      year: option.year,
      month: option.month,
      day: option.day,
      impact: option.impact,
      pageIndex: 0,
    });
  }

  strategyUpdated() {
    this.load();
  }

  private setChangeParameters(changeParameters: ChangesParameters): void {
    this._changesParameters.set(changeParameters);
    this.load();
  }

  private load(): void {
    this.routerService
      .updateQueryParams({
        strategy: this.state.preferences.strategy(),
        ...this.changesParameters(),
      })
      .then(() => {
        this.apiService
          .changes(this.state.preferences.strategy(), this.changesParameters())
          .subscribe((response) => this._response.set(response));
      });
  }
}
