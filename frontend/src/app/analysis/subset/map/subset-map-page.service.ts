import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { SubsetMapPage } from '@api/common/subset';
import { ApiResponse } from '@api/custom';
import { ApiService } from '@app/services';
import { RouterService } from '../../../shared/services/router.service';
import { SubsetService } from '../subset.service';
import { SubsetMapService } from './subset-map.service';

export class SubsetMapPageService {
  private readonly apiService = inject(ApiService);
  private readonly subsetService = inject(SubsetService);
  private readonly subsetMapService = inject(SubsetMapService);
  private readonly routerService = inject(RouterService);

  private readonly _response = signal<ApiResponse<SubsetMapPage>>(null);

  readonly response = this._response.asReadonly();

  onInit(): void {
    this.subsetService.initPage(this.routerService);
    this.load();
  }

  afterViewInit(): void {
    const response = this.response();
    this.subsetMapService.init(response.result.networks, response.result.bounds);
  }

  onDestroy(): void {
    this.subsetMapService.destroy();
  }

  private load(): void {
    this.apiService.subsetMap(this.subsetService.subset()).subscribe((response) => {
      if (response.result) {
        this.subsetService.setSubsetInfo(response.result.subsetInfo);
      }
      this._response.set(response);
    });
  }
}
