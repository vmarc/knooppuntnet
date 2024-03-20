import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { LocationChangesParameters } from '@api/common/location';
import { LocationChangesPage } from '@api/common/location';
import { ApiResponse } from '@api/custom';
import { ApiService } from '@app/services';
import { RouterService } from '../../../shared/services/router.service';
import { LocationService } from '../location.service';

export class LocationChangesPageService {
  private readonly apiService = inject(ApiService);
  private readonly locationService = inject(LocationService);
  private readonly routerService = inject(RouterService);

  private readonly _response = signal<ApiResponse<LocationChangesPage> | null>(null);
  private readonly _changesParameters = signal<LocationChangesParameters>(null);

  readonly response = this._response.asReadonly();
  readonly changesParameters = this._changesParameters.asReadonly();

  onInit() {
    this.locationService.initPage(this.routerService);
    const parameters: LocationChangesParameters = {
      pageSize: 25, // TODO SIGNAL take from preferences + query params
      pageIndex: 0,
    };
    this._changesParameters.set(parameters);
    this.load();
  }

  private load() {
    this.apiService
      .locationChanges(this.locationService.key(), this.changesParameters())
      .subscribe((response) => {
        if (response.result) {
          this.locationService.setSummary(response.result.summary);
        }
        this._response.set(response);
      });
  }
}
