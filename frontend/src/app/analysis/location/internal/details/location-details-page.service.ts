import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { LocationDetailsPage } from '@api/common/location/location-details-page';
import { ApiResponse } from '@api/custom';
import { ApiService } from '@app/services';
import { RouterService } from '../../../../shared/services/router.service';
import { LocationService } from '../location.service';

export class LocationDetailsPageService {
  private readonly apiService = inject(ApiService);
  private readonly locationService = inject(LocationService);
  private readonly routerService = inject(RouterService);
  private readonly activatedRoute = inject(ActivatedRoute);

  private readonly _response = signal<ApiResponse<LocationDetailsPage>>(null);
  readonly response = this._response.asReadonly();

  constructor() {
    this.activatedRoute.params.subscribe(() => {
      if (this.response()) {
        this._response.set(null);
        this.locationService.initPage(this.routerService);
        this.load();
      }
    });
  }

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
