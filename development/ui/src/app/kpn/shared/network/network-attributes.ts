// this class is generated, please do not modify

import {Country} from '../country';
import {Integrity} from './integrity';
import {LatLonImpl} from '../lat-lon-impl';
import {NetworkType} from '../network-type';
import {Timestamp} from '../timestamp';

export class NetworkAttributes {

  constructor(public id?: number,
              public country?: Country,
              public networkType?: NetworkType,
              public name?: string,
              public km?: number,
              public meters?: number,
              public nodeCount?: number,
              public routeCount?: number,
              public brokenRouteCount?: number,
              public brokenRoutePercentage?: string,
              public integrity?: Integrity,
              public unaccessibleRouteCount?: number,
              public connectionCount?: number,
              public lastUpdated?: Timestamp,
              public relationLastUpdated?: Timestamp,
              public center?: LatLonImpl) {
  }

  public static fromJSON(jsonObject): NetworkAttributes {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkAttributes();
    instance.id = jsonObject.id;
    instance.country = Country.fromJSON(jsonObject.country);
    instance.networkType = NetworkType.fromJSON(jsonObject.networkType);
    instance.name = jsonObject.name;
    instance.km = jsonObject.km;
    instance.meters = jsonObject.meters;
    instance.nodeCount = jsonObject.nodeCount;
    instance.routeCount = jsonObject.routeCount;
    instance.brokenRouteCount = jsonObject.brokenRouteCount;
    instance.brokenRoutePercentage = jsonObject.brokenRoutePercentage;
    instance.integrity = Integrity.fromJSON(jsonObject.integrity);
    instance.unaccessibleRouteCount = jsonObject.unaccessibleRouteCount;
    instance.connectionCount = jsonObject.connectionCount;
    instance.lastUpdated = Timestamp.fromJSON(jsonObject.lastUpdated);
    instance.relationLastUpdated = Timestamp.fromJSON(jsonObject.relationLastUpdated);
    instance.center = LatLonImpl.fromJSON(jsonObject.center);
    return instance;
  }
}

