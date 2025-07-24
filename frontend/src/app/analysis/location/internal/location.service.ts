import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { Country } from '@api/common/country';
import { LocationSummary } from '@api/common/location/location-summary';
import { RouteType } from '@api/common/route-type';
import { LocationKey } from '@api/custom/location-key';
import { LocationPageName } from '@app/analysis/location/internal/components/location-page';
import { RouterService } from '@app/shared/services/router.service';

@Injectable({
  providedIn: 'root',
})
export class LocationService {
  private readonly _key = signal<LocationKey | null>(null);
  private readonly _summary = signal<LocationSummary | null>(null);
  private readonly _pageName = signal<LocationPageName>(null);

  readonly key = this._key.asReadonly();
  readonly summary = this._summary.asReadonly();
  readonly pageName = this._pageName.asReadonly();

  onInit(routeType: RouteType, country: Country, location: string): void {
    const locationKey: LocationKey = {
      routeType,
      country,
      name: location,
    };
    if (this.shouldUpdate(this.key(), locationKey)) {
      this._key.set(locationKey);
      this._summary.set(null);
    }
  }

  initPage(routerService: RouterService) {
    const locationKey: LocationKey = {
      routeType: routerService.paramRouteType(),
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

  updatePageName(pageName: LocationPageName): void {
    this._pageName.set(pageName);
  }

  private shouldUpdate(oldKey: LocationKey, newKey: LocationKey): boolean {
    return (
      !oldKey ||
      oldKey.routeType !== newKey.routeType ||
      oldKey.country !== newKey.country ||
      oldKey.name !== newKey.name
    );
  }
}
