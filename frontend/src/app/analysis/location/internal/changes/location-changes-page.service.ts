import { computed } from '@angular/core';
import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { ChangesParameters } from '@api/common/changes/filter';
import { LocationChangesPage } from '@api/common/location';
import { ApiResponse } from '@api/custom';
import { PreferencesService } from '@app/core';
import { ChangeOption } from '@app/kpn/common';
import { ApiService } from '@app/services';
import { PageParams } from '@app/shared/base';
import { Util } from '@app/shared/components/util';
import { State } from '@app/state';
import { RouterService } from '../../../../shared/services/router.service';
import { UserService } from '../../../../shared/user';
import { LocationService } from '../location.service';

export class LocationChangesPageService {
  private readonly state = inject(State);
  private readonly apiService = inject(ApiService);
  private readonly locationService = inject(LocationService);
  private readonly routerService = inject(RouterService);
  private readonly preferencesService = inject(PreferencesService);
  private readonly userService = inject(UserService);

  private readonly _response = signal<ApiResponse<LocationChangesPage> | null>(null);
  private readonly _changesParameters = signal<ChangesParameters>(null);

  readonly loggedIn = this.userService.loggedIn;
  readonly impact = computed(() => this.changesParameters().impact);
  readonly pageSize = computed(() => this.changesParameters().pageSize);
  readonly pageIndex = computed(() => this.changesParameters().pageIndex);
  readonly filterOptions = computed(() => this.response()?.result?.filterOptions);

  readonly response = this._response.asReadonly();
  readonly changesParameters = this._changesParameters.asReadonly();

  onInit() {
    this.locationService.initPage(this.routerService);
    const uniqueQueryParams = Util.uniqueParams(this.routerService.queryParams());
    const pageParams = new PageParams(this.routerService.params(), uniqueQueryParams);
    const strategy = pageParams.strategy(this.state.preferences.strategy());
    const changesParameters = pageParams.changesParameters(
      this.state.preferences.impact(),
      this.state.preferences.pageSize()
    );
    this._changesParameters.set(changesParameters);
    this.load();
  }

  setPageSize(pageSize: number): void {
    this.state.preferences.updatePageSize(pageSize);
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

  private load() {
    this.routerService
      .updateQueryParams({
        strategy: this.state.preferences.strategy(),
        ...this.changesParameters(),
      })
      .then(() => {
        this.apiService
          .locationChanges(this.locationService.key(), this.changesParameters())
          .subscribe((response) => {
            if (response.result) {
              this.locationService.setSummary(response.result.summary);
            }
            this._response.set(response);
          });
      });
  }
}
