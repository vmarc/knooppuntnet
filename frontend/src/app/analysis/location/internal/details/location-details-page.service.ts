import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { LocationDetailsPage } from '@api/common/location/location-details-page';
import { ApiResponse } from '@api/custom/api-response';
import { ApiService } from '@app/shared/services/api.service';
import { LocationService } from '../location.service';

export class LocationDetailsPageService {
  private readonly apiService = inject(ApiService);
  private readonly locationService = inject(LocationService);

  private readonly _response = signal<ApiResponse<LocationDetailsPage>>(null);
  readonly response = this._response.asReadonly();

  onInit(): void {
    this.locationService.updatePageName('details');
    this.apiService.locationDetails(this.locationService.key()).subscribe((response) => {
      if (response.result) {
        this.locationService.setSummary(response.result.summary);
      }
      this._response.set(response);
    });
  }
}
