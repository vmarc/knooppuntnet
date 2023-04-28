import { Params } from '@angular/router';
import { LocationKey } from '@api/custom';
import { Countries } from '@app/kpn/common';
import { NetworkTypes } from '@app/kpn/common';

export class LocationParams {
  static toKey(params: Params): LocationKey {
    const networkType = NetworkTypes.withName(params['networkType']);
    const country = Countries.withDomain(params['country']);
    const name = params['location'];
    return {
      networkType,
      country,
      name,
    };
  }
}
