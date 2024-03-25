import { signal } from '@angular/core';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangesParameters } from '@api/common/changes/filter';
import { SubsetChangesPage } from '@api/common/subset';
import { ApiResponse } from '@api/custom';
import { Util } from '@app/components/shared';
import { PreferencesService } from '@app/core';
import { ChangeOption } from '@app/kpn/common';
import { ApiService } from '@app/services';
import { PageParams } from '@app/shared/base';
import { RouterService } from '../../../shared/services/router.service';
import { UserService } from '../../../shared/user';
import { SubsetService } from '../subset.service';

export class SubsetChangesPageService {
  private readonly apiService = inject(ApiService);
  private readonly subsetService = inject(SubsetService);
  private readonly preferencesService = inject(PreferencesService);
  private readonly routerService = inject(RouterService);
  private readonly userService = inject(UserService);

  private readonly _changesParameters = signal<ChangesParameters | null>(null);
  private readonly _response = signal<ApiResponse<SubsetChangesPage> | null>(null);

  readonly loggedIn = this.userService.loggedIn;
  readonly changesParameters = this._changesParameters.asReadonly();
  readonly response = this._response.asReadonly();
  readonly impact = computed(() => this.changesParameters().impact);
  readonly pageSize = computed(() => this.changesParameters().pageSize);
  readonly pageIndex = computed(() => this.changesParameters().pageIndex);
  readonly filterOptions = computed(() => this.response()?.result?.filterOptions);

  onInit(): void {
    this.subsetService.initPage(this.routerService);
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
    this.setChangesParameters({
      ...this.changesParameters(),
      pageIndex: 0,
      pageSize,
    });
  }

  setImpact(impact: boolean): void {
    this.preferencesService.setImpact(impact);
    this.setChangesParameters({
      ...this.changesParameters(),
      pageIndex: 0,
      impact,
    });
  }

  setPageIndex(pageIndex: number): void {
    this.setChangesParameters({
      ...this.changesParameters(),
      pageIndex,
    });
  }

  setFilterOption(option: ChangeOption): void {
    this.setChangesParameters({
      ...this.changesParameters(),
      year: option.year,
      month: option.month,
      day: option.day,
      impact: option.impact,
      pageIndex: 0,
    });
  }

  private setChangesParameters(changesParameters: ChangesParameters): void {
    this._changesParameters.set(changesParameters);
    this.load();
  }

  private load(): void {
    this.apiService
      .subsetChanges(this.subsetService.subset(), this.changesParameters())
      .subscribe((response) => {
        if (response.result) {
          this.subsetService.setSubsetInfo(response.result.subsetInfo);
        }
        this._response.set(response);
      });
  }
}
