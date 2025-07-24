import { computed } from '@angular/core';
import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { NetworkNodesPage } from '@api/common/network/network-nodes-page';
import { ApiResponse } from '@api/custom/api-response';
import { ApiService } from '@app/shared/services/api.service';
import { State } from '@app/state/state';
import { NetworkService } from '../network.service';
import { NetworkNodeFilter } from './components/network-node-filter';
import { NetworkNodeFilterCriteria } from './components/network-node-filter-criteria';

export class NetworkNodesPageService {
  private readonly state = inject(State);
  private readonly apiService = inject(ApiService);
  private readonly networkService = inject(NetworkService);

  private readonly _response = signal<ApiResponse<NetworkNodesPage>>(null);
  readonly response = this._response.asReadonly();

  private readonly _pageIndex = signal<number>(0);
  readonly pageIndex = this._pageIndex.asReadonly();

  readonly pageSize = this.state.preferences.pageSize;

  private readonly timeInfo = computed(() => this.response()?.result?.timeInfo);
  private readonly surveyDateInfo = computed(() => this.response()?.result?.surveyDateInfo);
  private readonly nodes = computed(() => this.response()?.result?.nodes ?? []);

  private readonly filterCriteria = signal<NetworkNodeFilterCriteria>(
    new NetworkNodeFilterCriteria()
  );

  private readonly filter = computed(
    () => new NetworkNodeFilter(this.filterCriteria, this.surveyDateInfo(), this.timeInfo())
  );

  readonly filteredNodes = computed(() => this.filter().filter(this.nodes()));

  readonly filterOptions = computed(() => this.filter().filterOptions(this.nodes()));

  readonly pageNodes = computed(() => {
    const pageIndex = this.pageIndex();
    const pageSize = this.pageSize();
    const start = pageIndex * pageSize;
    const end = start + pageSize;
    return this.filteredNodes()?.slice(start, end);
  });

  onInit(): void {
    this.networkService.updatePageName('nodes');
    this.apiService.networkNodes(this.networkService.networkId()).subscribe((response) => {
      if (response.result) {
        this.networkService.setSummary(response.result.summary);
      }
      this._response.set(response);
    });
  }

  updatePageSize(pageSize: number): void {
    this._pageIndex.set(0);
    this.state.preferences.updatePageSize(pageSize);
  }

  updatePageIndex(pageIndex: number): void {
    this._pageIndex.set(pageIndex);
  }
}
