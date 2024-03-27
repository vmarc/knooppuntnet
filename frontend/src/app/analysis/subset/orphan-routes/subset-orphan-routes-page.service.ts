import { computed } from '@angular/core';
import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { SubsetOrphanRoutesPage } from '@api/common/subset';
import { ApiResponse } from '@api/custom';
import { PreferencesService } from '@app/core';
import { ApiService } from '@app/services';
import { RouterService } from '../../../shared/services/router.service';
import { SubsetService } from '../subset.service';
import { SubsetOrphanRouteFilter } from './components/subset-orphan-route-filter';
import { SubsetOrphanRouteFilterCriteria } from './components/subset-orphan-route-filter-criteria';

export class SubsetOrphanRoutesPageService {
  private readonly apiService = inject(ApiService);
  private readonly subsetService = inject(SubsetService);
  private readonly preferencesService = inject(PreferencesService);
  private readonly routerService = inject(RouterService);

  private readonly _response = signal<ApiResponse<SubsetOrphanRoutesPage>>(null);
  private readonly _pageIndex = signal<number>(0);

  readonly response = this._response.asReadonly();
  readonly pageIndex = this._pageIndex.asReadonly();
  readonly pageSize = this.preferencesService.pageSize;
  readonly networkType = computed(() => this.response().result.subsetInfo.networkType);

  private readonly timeInfo = computed(() => this.response()?.result?.timeInfo);
  private readonly routes = computed(() => this.response()?.result?.routes ?? []);

  private readonly filterCriteria = signal<SubsetOrphanRouteFilterCriteria>(
    new SubsetOrphanRouteFilterCriteria()
  );

  private readonly filter = computed(() => {
    return new SubsetOrphanRouteFilter(this.filterCriteria, this.timeInfo());
  });

  readonly filteredRoutes = computed(() => {
    return this.filter().filter(this.routes());
  });

  readonly filterOptions = computed(() => {
    return this.filter().filterOptions(this.routes());
  });

  onInit(): void {
    this.subsetService.initPage(this.routerService);
    this.load();
  }

  setPageSize(pageSize: number): void {
    this.preferencesService.setPageSize(pageSize);
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
