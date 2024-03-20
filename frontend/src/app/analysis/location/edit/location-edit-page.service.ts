import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { LocationEditPage } from '@api/common/location';
import { ApiResponse } from '@api/custom';
import { ApiService } from '@app/services';
import { RouterService } from '../../../shared/services/router.service';
import { LocationService } from '../location.service';

export class LocationEditPageService {
  private readonly apiService = inject(ApiService);
  private readonly locationService = inject(LocationService);
  private readonly routerService = inject(RouterService);

  private readonly _response = signal<ApiResponse<LocationEditPage> | null>(null);
  readonly response = this._response.asReadonly();

  onInit() {
    this.locationService.initPage(this.routerService);
    this.load();
  }

  private load() {
    this.apiService.locationEdit(this.locationService.key()).subscribe((response) => {
      if (response.result) {
        this.locationService.setSummary(response.result.summary);
      }
      this._response.set(response);
    });
  }
}
