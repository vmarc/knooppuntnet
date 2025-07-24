import { computed } from '@angular/core';
import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { LocationMapPage } from '@api/common/location/location-map-page';
import { ApiResponse } from '@api/custom/api-response';
import { MapPosition } from '@app/ol/domain/map-position';
import { ApiService } from '@app/shared/services/api.service';
import { SharedStateService } from '@app/shared/core/shared/shared-state.service';
import { RouterService } from '@app/shared/services/router.service';
import { LocationService } from '../location.service';
import { LocationMapService } from './components/location-map.service';

export class LocationMapPageService {
  private readonly apiService = inject(ApiService);
  private readonly locationService = inject(LocationService);
  private readonly locationMapService = inject(LocationMapService);
  private readonly routerService = inject(RouterService);
  private readonly sharedStateService = inject(SharedStateService);

  private readonly _response = signal<ApiResponse<LocationMapPage> | null>(null);
  readonly response = this._response.asReadonly();
  readonly bounds = computed(() => this.response()?.result?.bounds);

  onInit() {
    this.locationService.updatePageName('map');
    this.apiService.locationMap(this.locationService.key()).subscribe((response) => {
      if (response.result) {
        this.locationService.setSummary(response.result.summary);
      }
      this._response.set(response);
    });
  }

  afterViewInit() {
    const geoJson = this.response().result.geoJson;
    const geoJson2 = this.response().result.geoJson2;
    const bounds = this.response().result.bounds;
    let mapPositionFromUrl: MapPosition = undefined;
    const mapPositionString = this.routerService.queryParam('position');
    if (mapPositionString) {
      mapPositionFromUrl = MapPosition.fromQueryParam(mapPositionString);
    }
    this.locationMapService.init(
      this.locationService.key(),
      this.sharedStateService.surveyDateValues(),
      geoJson,
      geoJson2,
      bounds,
      mapPositionFromUrl,
      this.routerService.urlLayerIds()
    );
  }
}
