import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { NetworkMapPage } from '@api/common/network';
import { ApiResponse } from '@api/custom';
import { MapPosition } from '@app/ol/domain';
import { NetworkMapPosition } from '@app/ol/domain';
import { ApiService } from '@app/services';
import { RouterService } from '../../../shared/services/router.service';
import { NetworkService } from '../network.service';
import { NetworkMapService } from './components/network-map.service';

export type NetworkMapState = {
  response: ApiResponse<NetworkMapPage>;
  mapPositionFromUrl: NetworkMapPosition;
};

export const initialState: NetworkMapState = {
  response: null,
  mapPositionFromUrl: null,
};

export class NetworkMapPageService {
  private readonly apiService = inject(ApiService);
  private readonly networkService = inject(NetworkService);
  private readonly networkMapService = inject(NetworkMapService);
  private readonly routerService = inject(RouterService);

  private readonly _response = signal<ApiResponse<NetworkMapPage>>(null);
  readonly response = this._response.asReadonly();

  private readonly _mapPositionFromUrl = signal<NetworkMapPosition>(null);
  readonly mapPositionFromUrl = this._mapPositionFromUrl.asReadonly();

  readonly networkId = this.networkService.networkId;

  onInit(): void {
    this.networkService.initPage(this.routerService);
    this.load();
  }

  onDestroy(): void {
    this.networkMapService.destroy();
  }

  afterViewInit(): void {
    let mapPositionFromUrl: NetworkMapPosition = undefined;
    const mapPositionString = this.routerService.queryParam('position');
    if (mapPositionString) {
      const mapPosition = MapPosition.fromQueryParam(mapPositionString);
      if (mapPosition) {
        mapPositionFromUrl = mapPosition.toNetworkMapPosition(
          mapPosition,
          this.networkService.networkId()
        );
      }
    }

    this.networkMapService.init(
      this.networkService.networkId(),
      this.response().result,
      mapPositionFromUrl
    );
  }

  private load() {
    this.apiService.networkMap(this.networkService.networkId()).subscribe((response) => {
      if (response.result) {
        this.networkService.setSummary(response.result.summary);
      }
      this._response.set(response);
    });
  }
}
