import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { LocationEditPage } from '@api/common/location/location-edit-page';
import { ApiResponse } from '@api/custom/api-response';
import { ApiService } from '@app/shared/services/api.service';
import { LocationService } from '../location.service';

export class LocationEditPageService {
  private readonly apiService = inject(ApiService);
  private readonly locationService = inject(LocationService);

  private readonly _response = signal<ApiResponse<LocationEditPage> | null>(null);
  readonly response = this._response.asReadonly();

  onInit() {
    this.locationService.updatePageName('edit');
    this.apiService.locationEdit(this.locationService.key()).subscribe((response) => {
      if (response.result) {
        this.locationService.setSummary(response.result.summary);
      }
      this._response.set(response);
    });
  }
}
