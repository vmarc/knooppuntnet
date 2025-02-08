import { computed } from '@angular/core';
import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { SubsetOrphanRoutesPage } from '@api/common/subset/subset-orphan-routes-page';
import { ApiResponse } from '@api/custom/api-response';
import { ApiService } from '@app/services';
import { State } from '@app/state/state';
import { RouterService } from '../../../../shared/services/router.service';
import { SubsetService } from '../subset.service';
import { SubsetOrphanRouteFilter } from './components/subset-orphan-route-filter';
import { SubsetOrphanRouteFilterCriteria } from './components/subset-orphan-route-filter-criteria';

export class SubsetOrphanRoutesPageService {
  private readonly state = inject(State);
  private readonly apiService = inject(ApiService);
  private readonly subsetService = inject(SubsetService);
  private readonly routerService = inject(RouterService);

  private readonly _response = signal<ApiResponse<SubsetOrphanRoutesPage>>(null);
  private readonly _pageIndex = signal<number>(0);

  readonly response = this._response.asReadonly();
  readonly pageIndex = this._pageIndex.asReadonly();
  readonly pageSize = this.state.preferences.pageSize;
  readonly routeType = computed(() => this.response().result.subsetInfo.routeType);

  private readonly timeInfo = computed(() => this.response()?.result?.timeInfo);
  private readonly routes = computed(() => this.response()?.result?.routes ?? []);

  private readonly filterCriteria = signal<SubsetOrphanRouteFilterCriteria>(
    new SubsetOrphanRouteFilterCriteria()
  );

  private readonly filter = computed(
    () => new SubsetOrphanRouteFilter(this.filterCriteria, this.timeInfo())
  );

  readonly filteredRoutes = computed(() => this.filter().filter(this.routes()));

  readonly filterOptions = computed(() => this.filter().filterOptions(this.routes()));

  onInit(): void {
    this.subsetService.initPage(this.routerService);
    this.load();
  }

  updatePageSize(pageSize: number): void {
    this.state.preferences.updatePageSize(pageSize);
  }

  private load(): void {
    this.apiService.subsetOrphanRoutes(this.subsetService.subset()).subscribe((response) => {
      if (response.result) {
        this.subsetService.setSubsetInfo(response.result.subsetInfo);
      }
      this._response.set(response);
    });
  }
}
