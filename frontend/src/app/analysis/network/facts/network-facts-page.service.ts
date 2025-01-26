import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { NetworkFactsPage } from '@api/common/network';
import { ApiResponse } from '@api/custom';
import { ApiService } from '@app/services';
import { State } from '@app/state';
import { RouterService } from '../../../shared/services/router.service';
import { NetworkService } from '../network.service';

export class NetworkFactsPageService {
  private readonly state = inject(State);
  private readonly apiService = inject(ApiService);
  private readonly networkService = inject(NetworkService);
  private readonly routerService = inject(RouterService);

  private readonly _response = signal<ApiResponse<NetworkFactsPage>>(null);
  readonly response = this._response.asReadonly();

  readonly pageSize = this.state.preferences.pageSize;

  onInit(): void {
    this.networkService.initPage(this.routerService);
    this.load();
  }

  private load(): void {
    this.apiService.networkFacts(this.networkService.networkId()).subscribe((response) => {
      if (response.result) {
        this.networkService.setSummary(response.result.summary);
      }
      this._response.set(response);
    });
  }
}
