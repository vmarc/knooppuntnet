import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { LocationSummary } from '@api/common/location';
import { LocationKey } from '@api/custom';
import { RouterService } from '../../shared/services/router.service';

@Injectable({
  providedIn: 'root',
})
export class LocationService {
  private readonly _key = signal<LocationKey | null>(null);
  private readonly _summary = signal<LocationSummary | null>(null);

  readonly key = this._key.asReadonly();
  readonly summary = this._summary.asReadonly();

  initPage(routerService: RouterService) {
    const locationKey: LocationKey = {
      networkType: routerService.paramNetworkType(),
      country: routerService.paramCountry(),
      name: routerService.param('location'),
    };
    if (this.shouldUpdate(this.key(), locationKey)) {
      this._key.set(locationKey);
      this._summary.set(null);
    }
  }

  setSummary(summary: LocationSummary): void {
    this._summary.set(summary);
  }

  private shouldUpdate(oldKey: LocationKey, newKey: LocationKey): boolean {
    return (
      !oldKey ||
      oldKey.networkType !== newKey.networkType ||
      oldKey.country !== newKey.country ||
      oldKey.name !== newKey.name
    );
  }
}
