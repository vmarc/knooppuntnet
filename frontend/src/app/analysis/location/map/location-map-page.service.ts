import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { LocationMapPage } from '@api/common/location';
import { ApiResponse } from '@api/custom';
import { MapPosition } from '@app/ol/domain';
import { ApiService } from '@app/services';
import { RouterService } from '../../../shared/services/router.service';
import { LocationService } from '../location.service';
import { LocationMapService } from './components/location-map.service';

export class LocationMapPageService {
  private readonly apiService = inject(ApiService);
  private readonly locationService = inject(LocationService);
  private readonly locationMapService = inject(LocationMapService);
  private readonly routerService = inject(RouterService);

  private readonly _response = signal<ApiResponse<LocationMapPage> | null>(null);
  readonly response = this._response.asReadonly();

  onInit() {
    this.locationService.initPage(this.routerService);
    this.load();
  }

  afterViewInit() {
    const geoJson = this.response().result.geoJson;
    const bounds = this.response().result.bounds;
    const mapPositionFromUrl: MapPosition = undefined; // TODO routerService.queryParamMapPosition();
    const surveyDateValues = null; // TODO eliminate surveyDateValues
    this.locationMapService.init(
      this.locationService.key().networkType,
      surveyDateValues,
      geoJson,
      bounds,
      mapPositionFromUrl
    );
  }

  private load() {
    this.apiService.locationMap(this.locationService.key()).subscribe((response) => {
      if (response.result) {
        this.locationService.setSummary(response.result.summary);
      }
      this._response.set(response);
    });
  }
}
