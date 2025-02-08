import { computed } from '@angular/core';
import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { NetworkNodesPage } from '@api/common/network/network-nodes-page';
import { ApiResponse } from '@api/custom/api-response';
import { ApiService } from '@app/services';
import { State } from '@app/state';
import { RouterService } from '../../../../shared/services/router.service';
import { NetworkService } from '../network.service';
import { NetworkNodeFilter } from './components/network-node-filter';
import { NetworkNodeFilterCriteria } from './components/network-node-filter-criteria';

export class NetworkNodesPageService {
  private readonly state = inject(State);
  private readonly apiService = inject(ApiService);
  private readonly networkService = inject(NetworkService);
  private readonly routerService = inject(RouterService);

  private readonly _selectedTabIndex = signal<number>(0);
  readonly selectedTabIndex = this._selectedTabIndex.asReadonly();

  private readonly _response = signal<ApiResponse<NetworkNodesPage>>(null);
  readonly response = this._response.asReadonly();

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

  onInit(): void {
    this.networkService.initPage(this.routerService);
    this.load();
  }

  updatePageSize(pageSize: number): void {
    this.state.preferences.updatePageSize(pageSize);
    this.load();
  }

  private load(): void {
    this.apiService.networkNodes(this.networkService.networkId()).subscribe((response) => {
      if (response.result) {
        this.networkService.setSummary(response.result.summary);
      }
      this._response.set(response);
    });
  }
}
