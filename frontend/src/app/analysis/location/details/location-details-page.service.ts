import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { LocationDetailsPage } from '@api/common/location/location-details-page';
import { ApiResponse } from '@api/custom';
import { LocationService } from '@app/analysis/location';
import { ApiService } from '@app/services';
import { RouterService } from '../../../shared/services/router.service';

export class LocationDetailsPageService {
  private readonly apiService = inject(ApiService);
  private readonly locationService = inject(LocationService);
  private readonly routerService = inject(RouterService);

  private readonly _response = signal<ApiResponse<LocationDetailsPage>>(null);
  readonly response = this._response.asReadonly();

  onInit(): void {
    this.locationService.initPage(this.routerService);
    this.load();
  }

  private load(): void {
    this.apiService.locationDetails(this.locationService.key()).subscribe((response) => {
      if (response.result) {
        this.locationService.setSummary(response.result.summary);
      }
      this._response.set(response);
    });
  }
}
