import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { SubsetNetworksPage } from '@api/common/subset';
import { ApiResponse } from '@api/custom';
import { ApiService } from '@app/services';
import { RouterService } from '../../../shared/services/router.service';
import { SubsetService } from '../subset.service';

export class SubsetNetworksPageService {
  private readonly apiService = inject(ApiService);
  private readonly subsetService = inject(SubsetService);
  private readonly routerService = inject(RouterService);

  private readonly _response = signal<ApiResponse<SubsetNetworksPage>>(null);
  readonly response = this._response.asReadonly();

  onInit(): void {
    this.subsetService.initPage(this.routerService);
    this.load();
  }

  private load(): void {
    this.apiService.subsetNetworks(this.subsetService.subset()).subscribe((response) => {
      if (response.result) {
        this.subsetService.setSubsetInfo(response.result.subsetInfo);
      }
      this._response.set(response);
    });
  }
}
