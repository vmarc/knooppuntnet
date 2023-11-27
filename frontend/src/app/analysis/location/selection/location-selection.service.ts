import { Injectable } from '@angular/core';
import { LocationNode } from '@api/common/location';
import { Country } from '@api/custom';
import { NetworkType } from '@api/custom';
import { ApiService } from '@app/services';
import { Observable } from 'rxjs';
import { shareReplay } from 'rxjs/operators';
import { map } from 'rxjs/operators';

@Injectable()
export class LocationSelectionService {
  private locationsCache: Map<string, Observable<LocationNode>> = new Map();

  constructor(private apiService: ApiService) {}

  locations(networkType: NetworkType, country: Country): Observable<LocationNode> {
    const key = `${networkType}:${country}`;
    if (!this.locationsCache.has(key)) {
      const res = this.apiService.locations(networkType, country).pipe(
        map((response) => response.result.locationNode),
        shareReplay(1)
      );
      this.locationsCache.set(key, res);
    }
    return this.locationsCache.get(key);
  }
}
