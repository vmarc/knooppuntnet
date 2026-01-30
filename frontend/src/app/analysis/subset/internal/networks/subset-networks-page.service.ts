import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { SubsetNetworksPage } from '@api/common/subset/subset-networks-page';
import { ApiResponse } from '@api/custom/api-response';
import { NetworkMarker } from '@app/analysis/subset/internal/networks/network-marker';
import { MapService } from '@app/map/map.service';
import { ApiService } from '@app/shared/services/api.service';
import { SubsetService } from '../subset.service';

export class SubsetNetworksPageService {
  private readonly apiService = inject(ApiService);
  private readonly subsetService = inject(SubsetService);
  private readonly mapService = inject(MapService);
  private readonly router = inject(Router);

  private readonly _response = signal<ApiResponse<SubsetNetworksPage>>(null);
  readonly response = this._response.asReadonly();

  private markers: NetworkMarker[] = [];

  onInit(): void {
    this.subsetService.setPageName('networks');
    this.apiService.subsetNetworks(this.subsetService.subset()).subscribe((response) => {
      if (response.result) {
        this.subsetService.setSubsetInfo(response.result.subsetInfo);
        this.addMarkers(response.result);
      }
      this._response.set(response);
    });
  }

  onDestroy(): void {
    this.removeMarkers();
  }

  private addMarkers(page: SubsetNetworksPage): void {
    const bounds = page.bounds;
    this.markers = page.networks.map((network) => new NetworkMarker(this.router, network));
    this.markers.forEach((marker) => this.mapService.addMarker(marker.marker));
    this.mapService.fitBounds(bounds);
  }

  private removeMarkers(): void {
    this.markers.forEach((marker) => marker.remove());
    this.markers = [];
  }
}
