import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {shareReplay} from 'rxjs/operators';
import {map} from 'rxjs/operators';
import {AppService} from '../../../app.service';
import {LocationNode} from '@api/common/location/location-node';
import {Country} from '@api/custom/country';
import {NetworkType} from '@api/custom/network-type';

@Injectable()
export class LocationSelectionService {

  private locationsCache: Map<string, Observable<LocationNode>> = new Map();

  constructor(private appService: AppService) {
  }

  locations(networkType: NetworkType, country: Country): Observable<LocationNode> {
    const key = `${networkType.name}:${country}`;
    if (!this.locationsCache.has(key)) {
      const res = this.appService.locations(networkType, country).pipe(
        map(response => response.result.locationNode),
        shareReplay(1)
      );
      this.locationsCache.set(key, res);
    }
    return this.locationsCache.get(key);
  }

}
