import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { SubsetMapPage } from '@api/common/subset/subset-map-page';
import { ApiResponse } from '@api/custom/api-response';
import { MapPosition } from '@app/ol/domain/map-position';
import { ApiService } from '@app/shared/services/api.service';
import { RouterService } from '@app/shared/services/router.service';
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
    this.apiService.subsetMap(this.subsetService.subset()).subscribe((response) => {
      if (response.result) {
        this.subsetService.setSubsetInfo(response.result.subsetInfo);
      }
      this._response.set(response);
    });
  }

  afterViewInit(): void {
    const mapPositionString = this.routerService.queryParam('position');
    const mapPositionFromUrl = MapPosition.fromQueryParam(mapPositionString);
    const response = this.response();
    this.subsetMapService.init(
      response.result.networks,
      response.result.bounds,
      mapPositionFromUrl,
      this.routerService.urlLayerIds()
    );
  }

  onDestroy(): void {
    this.subsetMapService.destroy();
  }
}
