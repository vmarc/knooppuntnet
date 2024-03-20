import { signal } from '@angular/core';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { LocationNodesParameters } from '@api/common/location';
import { LocationNodesPage } from '@api/common/location';
import { ApiResponse } from '@api/custom';
import { LocationNodesType } from '@api/custom';
import { PreferencesService } from '@app/core';
import { ApiService } from '@app/services';
import { RouterService } from '../../../shared/services/router.service';
import { LocationService } from '../location.service';

export class LocationNodesPageService {
  private readonly apiService = inject(ApiService);
  private readonly locationService = inject(LocationService);
  private readonly preferencesService = inject(PreferencesService);
  private readonly routerService = inject(RouterService);

  private readonly _response = signal<ApiResponse<LocationNodesPage> | null>(null);
  private readonly _pageType = signal<LocationNodesType>(LocationNodesType.all);
  private readonly _pageIndex = signal<number>(0);

  readonly response = this._response.asReadonly();
  readonly pageType = this._pageType.asReadonly();
  readonly pageIndex = this._pageIndex.asReadonly();
  readonly networkType = computed(() => this.locationService.key().networkType);
  readonly pageSize = this.preferencesService.pageSize;

  onInit(): void {
    this.locationService.initPage(this.routerService);
    this.load();
  }

  setPageSize(pageSize: number): void {
    this.preferencesService.setPageSize(pageSize);
    this.load();
  }

  setPageIndex(pageIndex: number): void {
    this._pageIndex.set(pageIndex);
    this.load();
  }

  setPageType(pageType: LocationNodesType): void {
    this._pageType.set(pageType);
    this.load();
  }

  private load(): void {
    const parameters: LocationNodesParameters = {
      locationNodesType: this.pageType(),
      pageSize: this.preferencesService.pageSize(),
      pageIndex: this.pageIndex(),
    };
    this.apiService.locationNodes(this.locationService.key(), parameters).subscribe((response) => {
      if (response.result) {
        this.locationService.setSummary(response.result.summary);
      }
      this._response.set(response);
    });
  }
}
