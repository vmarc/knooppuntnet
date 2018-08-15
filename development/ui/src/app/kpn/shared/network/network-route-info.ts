// this class is generated, please do not modify

import {Fact} from '../fact';
import {Timestamp} from '../timestamp';

export class NetworkRouteInfo {

  constructor(public id?: number,
              public name?: string,
              public wayCount?: number,
              public length?: number,
              public role?: string,
              public relationLastUpdated?: Timestamp,
              public lastUpdated?: Timestamp,
              public facts?: Array<Fact>) {
  }

  public static fromJSON(jsonObject): NetworkRouteInfo {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkRouteInfo();
    instance.id = jsonObject.id;
    instance.name = jsonObject.name;
    instance.wayCount = jsonObject.wayCount;
    instance.length = jsonObject.length;
    instance.role = jsonObject.role;
    instance.relationLastUpdated = Timestamp.fromJSON(jsonObject.relationLastUpdated);
    instance.lastUpdated = Timestamp.fromJSON(jsonObject.lastUpdated);
    instance.facts = jsonObject.facts ? jsonObject.facts.map(json => Fact.fromJSON(json)) : [];
    return instance;
  }
}

