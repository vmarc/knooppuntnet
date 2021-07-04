import { Params } from '@angular/router';
import { LocationKey } from '@api/custom/location-key';
import { NetworkType } from '@api/custom/network-type';
import { Countries } from '../../../kpn/common/countries';

export class LocationParams {
  static toKey(params: Params): LocationKey {
    const networkType = NetworkType.withName(params['networkType']);
    const country = Countries.withDomain(params['country']);
    const name = params['location'];
    return new LocationKey(networkType, country, name);
  }
}
