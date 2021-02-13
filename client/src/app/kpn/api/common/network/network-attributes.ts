// this class is generated, please do not modify

import {Country} from '../../custom/country';
import {Integrity} from './integrity';
import {LatLonImpl} from '../lat-lon-impl';
import {NetworkType} from '../../custom/network-type';
import {Timestamp} from '../../custom/timestamp';

export class NetworkAttributes {

  constructor(readonly id: number,
              readonly country: Country,
              readonly networkType: NetworkType,
              readonly name: string,
              readonly km: number,
              readonly meters: number,
              readonly nodeCount: number,
              readonly routeCount: number,
              readonly brokenRouteCount: number,
              readonly brokenRoutePercentage: string,
              readonly integrity: Integrity,
              readonly unaccessibleRouteCount: number,
              readonly connectionCount: number,
              readonly lastUpdated: Timestamp,
              readonly relationLastUpdated: Timestamp,
              readonly center: LatLonImpl) {
  }

  public static fromJSON(jsonObject: any): NetworkAttributes {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkAttributes(
      jsonObject.id,
      jsonObject.country,
      jsonObject.networkType,
      jsonObject.name,
      jsonObject.km,
      jsonObject.meters,
      jsonObject.nodeCount,
      jsonObject.routeCount,
      jsonObject.brokenRouteCount,
      jsonObject.brokenRoutePercentage,
      Integrity.fromJSON(jsonObject.integrity),
      jsonObject.unaccessibleRouteCount,
      jsonObject.connectionCount,
      Timestamp.fromJSON(jsonObject.lastUpdated),
      Timestamp.fromJSON(jsonObject.relationLastUpdated),
      LatLonImpl.fromJSON(jsonObject.center)
    );
  }
}
