import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { LocationFactsPage } from '@api/common/location/location-facts-page';
import { ApiResponse } from '@api/custom/api-response';
import { ApiService } from '@app/shared/services/api.service';
import { RouterService } from '@app/shared/services/router.service';
import { LocationService } from '../location.service';

export class LocationFactsPageService {
  private readonly apiService = inject(ApiService);
  private readonly locationService = inject(LocationService);
  private readonly routerService = inject(RouterService);

  private readonly _response = signal<ApiResponse<LocationFactsPage> | null>(null);
  readonly response = this._response.asReadonly();

  onInit() {
    this.locationService.initPage(this.routerService);
    this.load();
  }

  private load() {
    this.apiService.locationFacts(this.locationService.key()).subscribe((response) => {
      if (response.result) {
        this.locationService.setSummary(response.result.summary);
      }
      this._response.set(response);
    });
  }
}
