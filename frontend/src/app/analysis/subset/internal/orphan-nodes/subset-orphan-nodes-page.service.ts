import { computed } from '@angular/core';
import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { SubsetOrphanNodesPage } from '@api/common/subset/subset-orphan-nodes-page';
import { ApiResponse } from '@api/custom/api-response';
import { ApiService } from '@app/shared/services/api.service';
import { State } from '@app/state/state';
import { RouterService } from '@app/shared/services/router.service';
import { SubsetService } from '../subset.service';
import { SubsetOrphanNodeFilter } from './components/subset-orphan-node-filter';
import { SubsetOrphanNodeFilterCriteria } from './components/subset-orphan-node-filter-criteria';

export class SubsetOrphanNodesPageService {
  private readonly state = inject(State);
  private readonly apiService = inject(ApiService);
  private readonly subsetService = inject(SubsetService);
  private readonly routerService = inject(RouterService);

  private readonly _response = signal<ApiResponse<SubsetOrphanNodesPage>>(null);
  private readonly _pageIndex = signal<number>(0);

  readonly response = this._response.asReadonly();
  readonly pageIndex = this._pageIndex.asReadonly();
  readonly pageSize = this.state.preferences.pageSize;

  private readonly filterCriteria = signal(new SubsetOrphanNodeFilterCriteria());
  private readonly timeInfo = computed(() => this.response()?.result?.timeInfo);
  private readonly nodes = computed(() => this.response()?.result?.nodes ?? []);

  private readonly filter = computed(() => {
    return new SubsetOrphanNodeFilter(this.filterCriteria, this.timeInfo());
  });

  readonly filteredNodes = computed(() => {
    return this.filter().filter(this.nodes());
  });

  readonly filterOptions = computed(() => {
    return this.filter().filterOptions(this.nodes());
  });

  onInit(): void {
    this.subsetService.initPage(this.routerService);
    this.load();
  }

  updatePageSize(pageSize: number): void {
    this.state.preferences.updatePageSize(pageSize);
  }

  private load(): void {
    this.apiService.subsetOrphanNodes(this.subsetService.subset()).subscribe((response) => {
      if (response.result) {
        this.subsetService.setSubsetInfo(response.result.subsetInfo);
      }
      this._response.set(response);
    });
  }
}
