import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { NetworkDetailsPage } from '@api/common/network/network-details-page';
import { ApiResponse } from '@api/custom/api-response';
import { MapService } from '@app/map/map.service';
import { ApiService } from '@app/shared/services/api.service';
import { State } from '@app/state/state';
import { NetworkService } from '../network.service';

export class NetworkDetailsPageService {
  private readonly state = inject(State);
  private readonly apiService = inject(ApiService);
  private readonly networkService = inject(NetworkService);
  private readonly mapService = inject(MapService);

  private readonly _response = signal<ApiResponse<NetworkDetailsPage>>(null);
  readonly response = this._response.asReadonly();

  readonly networkId = this.networkService.networkId;

  onInit(): void {
    this.networkService.updatePageName('details');
    this.apiService.networkDetails(this.networkService.networkId()).subscribe((response) => {
      if (response.result) {
        this.networkService.setSummary(response.result.summary);
        this.updateMap(response.result);
      }
      this._response.set(response);
    });
  }

  onDestroy(): void {
    this.mapService.resetRouteSelection(this.response().result.summary.routeType);
  }

  private updateMap(page: NetworkDetailsPage): void {
    this.state.map.updateMode('analysis');
    this.state.map.updateRouteType(page.summary.routeType);
    this.mapService.execute(() => {
      if (page.detail.bounds) {
        this.mapService.fitBounds(page.detail.bounds);
      }

      const longRouteIds: number[] = [...page.networkRouteIds, ...page.connectionRouteIds];
      const routeIds = longRouteIds.map((routeId) => routeId.toString());
      this.mapService.selectRoutes(page.summary.routeType, routeIds);
    });
  }
}
