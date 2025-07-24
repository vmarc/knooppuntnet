import { computed } from '@angular/core';
import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { NetworkRoutesPage } from '@api/common/network/network-routes-page';
import { ApiResponse } from '@api/custom/api-response';
import { ApiService } from '@app/shared/services/api.service';
import { State } from '@app/state/state';
import { NetworkService } from '../network.service';
import { NetworkRouteFilterCriteria } from './components/network-route-filter-criteria';
import { NetworkRouteFilter } from './components/network-route-filter';

export class NetworkRoutesPageService {
  private readonly state = inject(State);
  private readonly apiService = inject(ApiService);
  private readonly networkService = inject(NetworkService);

  private readonly _response = signal<ApiResponse<NetworkRoutesPage>>(null);
  readonly response = this._response.asReadonly();

  readonly pageSize = this.state.preferences.pageSize;
  private readonly timeInfo = computed(() => this.response()?.result?.timeInfo);
  private readonly surveyDateInfo = computed(() => this.response()?.result?.surveyDateInfo);
  private readonly routes = computed(() => this.response()?.result?.routes ?? []);
  readonly totalRouteCount = computed(() => this.routes().length);

  private readonly filterCriteria = signal<NetworkRouteFilterCriteria>(
    new NetworkRouteFilterCriteria()
  );

  private readonly filter = computed(
    () => new NetworkRouteFilter(this.filterCriteria, this.timeInfo(), this.surveyDateInfo())
  );

  readonly filteredRoutes = computed(() => this.filter().filter(this.routes()));

  readonly filterOptions = computed(() => this.filter().filterOptions(this.routes()));

  onInit(): void {
    this.networkService.updatePageName('routes');
    this.networkService.updateNetworkNotFound(false);
    this.apiService.networkRoutes(this.networkService.networkId()).subscribe((response) => {
      if (response.result) {
        this.networkService.setSummary(response.result.summary);
      } else {
        this.networkService.updateNetworkNotFound(true);
      }
      this._response.set(response);
    });
  }

  updatePageSize(pageSize: number): void {
    this.state.preferences.updatePageSize(pageSize);
  }
}
