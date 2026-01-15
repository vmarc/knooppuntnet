import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { NetworkDetailsPage } from '@api/common/network/network-details-page';
import { ApiResponse } from '@api/custom/api-response';
import { ApiService } from '@app/shared/services/api.service';
import { NetworkService } from '../network.service';

export class NetworkDetailsPageService {
  private readonly apiService = inject(ApiService);
  private readonly networkService = inject(NetworkService);

  private readonly _response = signal<ApiResponse<NetworkDetailsPage>>(null);
  readonly response = this._response.asReadonly();

  readonly networkId = this.networkService.networkId;

  onInit(): void {
    this.networkService.updatePageName('details');
    this.apiService.networkDetails(this.networkService.networkId()).subscribe((response) => {
      if (response.result) {
        this.networkService.setSummary(response.result.summary);
      }
      this._response.set(response);
    });
  }
}
