import { computed } from '@angular/core';
import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { SubsetFactDetailsPage } from '@api/common/subset/subset-fact-details-page';
import { ApiResponse } from '@api/custom/api-response';
import { Facts } from '@app/analysis/fact/components/facts';
import { SubsetFact } from '@app/shared/kpn/common/subset-fact';
import { ApiService } from '@app/shared/services/api.service';
import { PageParams } from '@app/shared/base/page-params';
import { RouterService } from '@app/shared/services/router.service';
import { SubsetService } from '../subset.service';

export class SubsetFactDetailsPageService {
  private readonly apiService = inject(ApiService);
  private readonly subsetService = inject(SubsetService);
  private readonly routerService = inject(RouterService);

  private readonly _subsetFact = signal<SubsetFact>(null);
  private readonly _response = signal<ApiResponse<SubsetFactDetailsPage>>(null);

  readonly subsetFact = this._subsetFact.asReadonly();
  readonly response = this._response.asReadonly();
  readonly page = computed(() => this.response()?.result);
  readonly factDefinition = computed(() => Facts.facts.get(this.subsetFact().fact));

  onInit(): void {
    this.subsetService.initPage(this.routerService);
    const subsetFact = new PageParams(this.routerService.params()).subsetFact();
    this._subsetFact.set(subsetFact);
    this.load();
  }

  private load(): void {
    this.apiService
      .subsetFactDetails(this.subsetService.subset(), this.subsetFact().fact)
      .subscribe((response) => {
        if (response.result) {
          this.subsetService.setSubsetInfo(response.result.subsetInfo);
        }
        this._response.set(response);
      });
  }
}
