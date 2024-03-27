import { computed } from '@angular/core';
import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { NetworkRoutesPage } from '@api/common/network';
import { ApiResponse } from '@api/custom';
import { PreferencesService } from '@app/core';
import { ApiService } from '@app/services';
import { RouterService } from '../../../shared/services/router.service';
import { NetworkService } from '../network.service';
import { NetworkRouteFilter } from './components/network-route-filter';
import { NetworkRouteFilterCriteria } from './components/network-route-filter-criteria';

export class NetworkRoutesPageService {
  private readonly apiService = inject(ApiService);
  private readonly networkService = inject(NetworkService);
  private readonly preferencesService = inject(PreferencesService);
  private readonly routerService = inject(RouterService);

  private readonly _response = signal<ApiResponse<NetworkRoutesPage>>(null);
  readonly response = this._response.asReadonly();

  readonly pageSize = this.preferencesService.pageSize;
  private readonly timeInfo = computed(() => this.response()?.result?.timeInfo);
  private readonly surveyDateInfo = computed(() => this.response()?.result?.surveyDateInfo);
  private readonly routes = computed(() => this.response()?.result?.routes ?? []);

  private readonly filterCriteria = signal<NetworkRouteFilterCriteria>(
    new NetworkRouteFilterCriteria()
  );

  private readonly filter = computed(
    () => new NetworkRouteFilter(this.filterCriteria, this.timeInfo(), this.surveyDateInfo())
  );

  readonly filteredRoutes = computed(() => this.filter().filter(this.routes()));

  readonly filterOptions = computed(() => this.filter().filterOptions(this.routes()));

  onInit(): void {
    this.networkService.initPage(this.routerService);
    this.load();
  }

  setPageSize(pageSize: number): void {
    this.preferencesService.setPageSize(pageSize);
  }

  private load(): void {
    this.apiService.networkRoutes(this.networkService.networkId()).subscribe((response) => {
      if (response.result) {
        this.networkService.setSummary(response.result.summary);
      }
      this._response.set(response);
    });
  }
}
