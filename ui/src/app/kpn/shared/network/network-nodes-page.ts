// this class is generated, please do not modify

import {NetworkNodeInfo2} from './network-node-info2';
import {NetworkSummary} from './network-summary';
import {NetworkType} from '../network-type';
import {TimeInfo} from '../time-info';

export class NetworkNodesPage {

  constructor(public timeInfo?: TimeInfo,
              public networkSummary?: NetworkSummary,
              public networkType?: NetworkType,
              public nodes?: Array<NetworkNodeInfo2>,
              public routeIds?: Array<number>) {
  }

  public static fromJSON(jsonObject): NetworkNodesPage {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkNodesPage();
    instance.timeInfo = TimeInfo.fromJSON(jsonObject.timeInfo);
    instance.networkSummary = NetworkSummary.fromJSON(jsonObject.networkSummary);
    instance.networkType = NetworkType.fromJSON(jsonObject.networkType);
    instance.nodes = jsonObject.nodes ? jsonObject.nodes.map(json => NetworkNodeInfo2.fromJSON(json)) : [];
    instance.routeIds = jsonObject.routeIds;
    return instance;
  }
}

