// this class is generated, please do not modify

import {Country} from './country';
import {NetworkType} from './network-type';
import {Tags} from './data/tags';
import {Timestamp} from './timestamp';

export class RouteSummary {

  constructor(public id?: number,
              public country?: Country,
              public networkType?: NetworkType,
              public name?: string,
              public meters?: number,
              public isBroken?: boolean,
              public wayCount?: number,
              public timestamp?: Timestamp,
              public nodeNames?: Array<string>,
              public tags?: Tags) {
  }

  public static fromJSON(jsonObject): RouteSummary {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new RouteSummary();
    instance.id = jsonObject.id;
    instance.country = jsonObject.country;
    instance.networkType = jsonObject.networkType;
    instance.name = jsonObject.name;
    instance.meters = jsonObject.meters;
    instance.isBroken = jsonObject.isBroken;
    instance.wayCount = jsonObject.wayCount;
    instance.timestamp = jsonObject.timestamp;
    instance.nodeNames = jsonObject.nodeNames;
    instance.tags = jsonObject.tags;
    return instance;
  }
}

