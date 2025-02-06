import { Params } from '@angular/router';
import { LocationKey } from '@api/custom';
import { Countries } from '@app/kpn/common';
import { RouteTypes } from '@app/kpn/common';

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
