// this class is generated, please do not modify

import {RoutesFact} from "../routes-fact";

export class NetworkRoutesFacts {

  constructor(public networkId?: number,
              public networkName?: string,
              public facts?: RoutesFact) {
  }

  public static fromJSON(jsonObject): NetworkRoutesFacts {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkRoutesFacts();
    instance.networkId = jsonObject.networkId;
    instance.networkName = jsonObject.networkName;
    instance.facts = RoutesFact.fromJSON(jsonObject.facts);
    return instance;
  }
}

