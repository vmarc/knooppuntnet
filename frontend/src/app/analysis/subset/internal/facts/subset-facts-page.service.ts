import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { SubsetFactsPage } from '@api/common/subset/subset-facts-page';
import { ApiResponse } from '@api/custom/api-response';
import { ApiService } from '@app/shared/services/api.service';
import { SubsetService } from '../subset.service';

export class SubsetFactsPageService {
  private readonly apiService = inject(ApiService);
  private readonly subsetService = inject(SubsetService);

  private readonly _response = signal<ApiResponse<SubsetFactsPage>>(null);

  readonly response = this._response.asReadonly();

  onInit(): void {
    this.apiService.subsetFacts(this.subsetService.subset()).subscribe((response) => {
      if (response.result) {
        this.subsetService.setSubsetInfo(response.result.subsetInfo);
      }
      this._response.set(response);
    });
  }
}
