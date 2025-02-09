import { Params } from '@angular/router';
import { LocationKey } from '@api/custom/location-key';
import { Countries } from '@app/shared/kpn/common/countries';
import { RouteTypes } from '@app/shared/kpn/common/route-types';

export class LocationParams {
  static toKey(params: Params): LocationKey {
    const routeType = RouteTypes.withName(params['routeType']);
    const country = Countries.withDomain(params['country']);
    const name = params['location'];
    return {
      routeType,
      country,
      name,
    };
  }
}
