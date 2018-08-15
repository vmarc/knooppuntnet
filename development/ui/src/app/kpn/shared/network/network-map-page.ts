// this class is generated, please do not modify

import {NetworkNodeInfo2} from './network-node-info2';
import {NetworkSummary} from './network-summary';

export class NetworkMapPage {

  constructor(public networkSummary?: NetworkSummary,
              public nodes?: Array<NetworkNodeInfo2>,
              public nodeIds?: Array<number>,
              public routeIds?: Array<number>) {
  }

  public static fromJSON(jsonObject): NetworkMapPage {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkMapPage();
    instance.networkSummary = NetworkSummary.fromJSON(jsonObject.networkSummary);
    instance.nodes = jsonObject.nodes ? jsonObject.nodes.map(json => NetworkNodeInfo2.fromJSON(json)) : [];
    instance.nodeIds = jsonObject.nodeIds;
    instance.routeIds = jsonObject.routeIds;
    return instance;
  }
}

