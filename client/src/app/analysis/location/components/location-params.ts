import { Params } from '@angular/router';
import { LocationKey } from '@api/custom/location-key';
import { Countries } from '../../../kpn/common/countries';
import { NetworkTypes } from '../../../kpn/common/network-types';

export class LocationParams {
  static toKey(params: Params): LocationKey {
    const networkType = NetworkTypes.withName(params['networkType']);
    const country = Countries.withDomain(params['country']);
    const name = params['location'];
    return new LocationKey(networkType, country, name);
  }
}
