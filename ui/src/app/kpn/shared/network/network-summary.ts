// this class is generated, please do not modify

import {NetworkType} from '../network-type';

export class NetworkSummary {

  constructor(public networkType?: NetworkType,
              public name?: string,
              public factCount?: number,
              public nodeCount?: number,
              public routeCount?: number) {
  }

  public static fromJSON(jsonObject): NetworkSummary {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkSummary();
    instance.networkType = jsonObject.networkType;
    instance.name = jsonObject.name;
    instance.factCount = jsonObject.factCount;
    instance.nodeCount = jsonObject.nodeCount;
    instance.routeCount = jsonObject.routeCount;
    return instance;
  }
}

