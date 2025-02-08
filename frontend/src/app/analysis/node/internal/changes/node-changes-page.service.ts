import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { computed } from '@angular/core';
import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
import { NodeChangesPage } from '@api/common/node/node-changes-page';
import { ApiResponse } from '@api/custom/api-response';
import { ChangeOption } from '@app/kpn/common';
import { ApiService } from '@app/services';
import { PageParams } from '@app/shared/base';
import { Util } from '@app/shared/components/util';
import { State } from '@app/state/state';
import { RouterService } from '../../../../shared/services/router.service';
import { UserService } from '../../../../shared/user';
import { NodeService } from '../node.service';

@Injectable()
export class NodeChangesPageService {
  private readonly state = inject(State);
  private readonly apiService = inject(ApiService);
  private readonly nodeService = inject(NodeService);
  private readonly routerService = inject(RouterService);
  private readonly userService = inject(UserService);

  readonly loggedIn = this.userService.loggedIn;

  private readonly _response = signal<ApiResponse<NodeChangesPage>>(null);
  readonly response = this._response.asReadonly();

  private readonly _changesParameters = signal<ChangesParameters>(null);
  readonly changesParameters = this._changesParameters.asReadonly();

  readonly impact = computed(() => this.changesParameters().impact);
  readonly pageSize = computed(() => this.changesParameters().pageSize);
  readonly pageIndex = computed(() => this.changesParameters().pageIndex);
  readonly filterOptions = computed(() => this.response()?.result?.filterOptions);

  onInit(): void {
    this.nodeService.initPage(this.routerService);
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
        .nodeChanges(this.nodeService.nodeId(), this.changesParameters())
        .subscribe((response) => {
          if (response.result) {
            this.nodeService.updateNode(response.result.nodeName, response.result.changeCount);
          }
          this._response.set(response);
        });
    });
  }
}
