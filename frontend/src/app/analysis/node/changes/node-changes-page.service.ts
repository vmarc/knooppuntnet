import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { computed } from '@angular/core';
import { ChangesParameters } from '@api/common/changes/filter';
import { NodeChangesPage } from '@api/common/node';
import { ApiResponse } from '@api/custom';
import { Util } from '@app/components/shared';
import { ChangeOption } from '@app/kpn/common';
import { ApiService } from '@app/services';
import { PageParams } from '@app/shared/base';
import { PreferencesStore } from '../../../shared/core/preferences/preferences.store';
import { RouterService } from '../../../shared/services/router.service';
import { UserStore } from '../../../shared/user/user.store';
import { NodeService } from '../node.service';

@Injectable()
export class NodeChangesPageService {
  private readonly apiService = inject(ApiService);
  private readonly nodeService = inject(NodeService);
  private readonly routerService = inject(RouterService);
  private readonly preferencesStore = inject(PreferencesStore);
  private readonly userStore = inject(UserStore);

  readonly loggedIn = this.userStore.loggedIn;

  private readonly _response = signal<ApiResponse<NodeChangesPage>>(null);
  readonly response = this._response.asReadonly();

  private readonly _changesParameters = signal<ChangesParameters>(null);
  readonly changesParameters = this._changesParameters.asReadonly();

  readonly impact = this.preferencesStore.impact;
  readonly pageSize = this.preferencesStore.pageSize;
  readonly pageIndex = computed(() => this.changesParameters()?.pageIndex);
  readonly filterOptions = computed(() => this.response()?.result?.filterOptions);

  onInit(): void {
    this.nodeService.initPage(this.routerService);
    const params = this.routerService.params();
    const queryParams = this.routerService.queryParams();
    const uniqueQueryParams = Util.uniqueParams(queryParams);
    const pageParams = new PageParams(params, uniqueQueryParams);
    const preferencesImpact = this.preferencesStore.impact();
    const preferencesPageSize = this.preferencesStore.pageSize();
    const changesParameters = pageParams.changesParameters(preferencesImpact, preferencesPageSize);
    this._changesParameters.set(changesParameters);
    this.load();
  }

  updatePageSize(pageSize: number): void {
    this.preferencesStore.updatePageSize(pageSize);
    this._changesParameters.set({
      ...this.changesParameters(),
      pageIndex: 0,
      pageSize,
    });
    this.load();
  }

  updateImpact(impact: boolean): void {
    this.preferencesStore.updateImpact(impact);
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
    this.apiService
      .nodeChanges(this.nodeService.nodeId(), this.changesParameters())
      .subscribe((response) => {
        if (response.result) {
          this.nodeService.updateNode(response.result.nodeName, response.result.changeCount);
        }
        this._response.set(response);
      });
  }
}
