import { signal } from '@angular/core';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { LocationRoutesParameters } from '@api/common/location';
import { LocationRoutesPage } from '@api/common/location';
import { LocationRoutesType } from '@api/custom';
import { ApiResponse } from '@api/custom';
import { PreferencesService } from '@app/core';
import { ApiService } from '@app/services';
import { RouterService } from '../../../shared/services/router.service';
import { LocationService } from '../location.service';

export class LocationRoutesPageService {
  private readonly apiService = inject(ApiService);
  private readonly locationService = inject(LocationService);
  private readonly preferencesService = inject(PreferencesService);
  private readonly routerService = inject(RouterService);

  private readonly _pageType = signal<LocationRoutesType | null>(null);
  private readonly _pageIndex = signal<number>(0);
  private readonly _response = signal<ApiResponse<LocationRoutesPage> | null>(null);

  readonly pageType = this._pageType.asReadonly();
  readonly pageIndex = this._pageIndex.asReadonly();
  readonly response = this._response.asReadonly();
  readonly networkType = computed(() => this.locationService.key().networkType);
  readonly pageSize = computed(() => this.preferencesService.pageSize());

  onInit(): void {
    this.locationService.initPage(this.routerService);
    this._pageType.set(LocationRoutesType.all); // TODO SIGNAL derive from query params?
    this.load();
  }

  setPageSize(pageSize: number): void {
    this.preferencesService.setPageSize(pageSize);
    this._pageIndex.set(0);
    this.load();
  }

  setPageIndex(pageIndex: number): void {
    this._pageIndex.set(pageIndex);
    this.load();
  }

  setPageType(pageType: LocationRoutesType): void {
    this._pageType.set(pageType);
    this._pageIndex.set(0);
    this.load();
  }

  private load(): void {
    const parameters: LocationRoutesParameters = {
      locationRoutesType: this.pageType(),
      pageSize: this.preferencesService.pageSize(),
      pageIndex: this.pageIndex(),
    };
    this.apiService.locationRoutes(this.locationService.key(), parameters).subscribe((response) => {
      if (response.result) {
        this.locationService.setSummary(response.result.summary);
      }
      this._response.set(response);
    });
  }
}
