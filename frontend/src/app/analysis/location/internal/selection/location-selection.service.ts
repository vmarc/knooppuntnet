import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { Country } from '@api/common';
import { RouteType } from '@api/common';
import { LocationNode } from '@api/common/location';
import { ApiService } from '@app/services';
import { Observable } from 'rxjs';
import { shareReplay } from 'rxjs/operators';
import { map } from 'rxjs/operators';

@Injectable()
export class LocationSelectionService {
  private readonly apiService = inject(ApiService);

  private locationsCache: Map<string, Observable<LocationNode>> = new Map();

  locations(routeType: RouteType, country: Country): Observable<LocationNode> {
    const key = `${routeType}:${country}`;
    if (!this.locationsCache.has(key)) {
      const res = this.apiService.locations(routeType, country).pipe(
        map((response) => response.result.locationNode),
        shareReplay(1)
      );
      this.locationsCache.set(key, res);
    }
    return this.locationsCache.get(key);
  }
}
