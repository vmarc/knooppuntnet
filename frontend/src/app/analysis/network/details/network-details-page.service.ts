import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { NetworkDetailsPage } from '@api/common/network';
import { ApiResponse } from '@api/custom';
import { PreferencesService } from '@app/core';
import { ApiService } from '@app/services';
import { RouterService } from '../../../shared/services/router.service';
import { NetworkService } from '../network.service';

export class NetworkDetailsPageService {
  private readonly apiService = inject(ApiService);
  private readonly networkService = inject(NetworkService);
  private readonly preferencesService = inject(PreferencesService);
  private readonly routerService = inject(RouterService);

  private readonly _response = signal<ApiResponse<NetworkDetailsPage>>(null);
  readonly response = this._response.asReadonly();

  onInit(): void {
    this.networkService.initPage(this.routerService);
    this.load();
  }

  private load(): void {
    this.apiService.networkDetails(this.networkService.networkId()).subscribe((response) => {
      if (response.result) {
        this.networkService.setSummary(response.result.summary);
      }
      this._response.set(response);
    });
  }
}
