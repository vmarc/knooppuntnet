import { computed } from '@angular/core';
import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { SubsetFactDetailsPage } from '@api/common/subset';
import { ApiResponse } from '@api/custom';
import { Facts } from '@app/analysis/fact';
import { SubsetFact } from '@app/kpn/common';
import { ApiService } from '@app/services';
import { PageParams } from '@app/shared/base';
import { RouterService } from '../../../shared/services/router.service';
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
  readonly factDefinition = computed(() => Facts.facts.get(this.subsetFact().factName));

  onInit(): void {
    this.subsetService.initPage(this.routerService);
    const subsetFact = new PageParams(this.routerService.params()).subsetFact();
    this._subsetFact.set(subsetFact);
    this.load();
  }

  private load(): void {
    this.apiService
      .subsetFactDetails(this.subsetService.subset(), this.subsetFact().factName)
      .subscribe((response) => {
        if (response.result) {
          this.subsetService.setSubsetInfo(response.result.subsetInfo);
        }
        this._response.set(response);
      });
  }
}
