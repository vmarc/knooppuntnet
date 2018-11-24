// this class is generated, please do not modify

import {Country} from '../country';
import {Integrity} from './integrity';
import {LatLonImpl} from '../lat-lon-impl';
import {NetworkType} from '../network-type';
import {Timestamp} from '../timestamp';

export class NetworkAttributes {
  readonly id: number;
  readonly country: Country;
  readonly networkType: NetworkType;
  readonly name: string;
  readonly km: number;
  readonly meters: number;
  readonly nodeCount: number;
  readonly routeCount: number;
  readonly brokenRouteCount: number;
  readonly brokenRoutePercentage: string;
  readonly integrity: Integrity;
  readonly unaccessibleRouteCount: number;
  readonly connectionCount: number;
  readonly lastUpdated: Timestamp;
  readonly relationLastUpdated: Timestamp;
  readonly center: LatLonImpl;

  constructor(id: number,
              country: Country,
              networkType: NetworkType,
              name: string,
              km: number,
              meters: number,
              nodeCount: number,
              routeCount: number,
              brokenRouteCount: number,
              brokenRoutePercentage: string,
              integrity: Integrity,
              unaccessibleRouteCount: number,
              connectionCount: number,
              lastUpdated: Timestamp,
              relationLastUpdated: Timestamp,
              center: LatLonImpl) {
    this.id = id;
    this.country = country;
    this.networkType = networkType;
    this.name = name;
    this.km = km;
    this.meters = meters;
    this.nodeCount = nodeCount;
    this.routeCount = routeCount;
    this.brokenRouteCount = brokenRouteCount;
    this.brokenRoutePercentage = brokenRoutePercentage;
    this.integrity = integrity;
    this.unaccessibleRouteCount = unaccessibleRouteCount;
    this.connectionCount = connectionCount;
    this.lastUpdated = lastUpdated;
    this.relationLastUpdated = relationLastUpdated;
    this.center = center;
  }

  public static fromJSON(jsonObject): NetworkAttributes {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkAttributes(
      jsonObject.id,
      Country.fromJSON(jsonObject.country),
      NetworkType.fromJSON(jsonObject.networkType),
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
