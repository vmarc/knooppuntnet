// this class is generated, please do not modify

import {NetworkFacts} from '../network-facts';
import {NetworkNodeInfo2} from './network-node-info2';
import {NetworkRouteInfo} from './network-route-info';
import {NetworkShape} from './network-shape';

export class NetworkInfoDetail {

  constructor(public nodes?: Array<NetworkNodeInfo2>,
              public routes?: Array<NetworkRouteInfo>,
              public networkFacts?: NetworkFacts,
              public shape?: NetworkShape) {
  }

  public static fromJSON(jsonObject): NetworkInfoDetail {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkInfoDetail();
    instance.nodes = jsonObject.nodes ? jsonObject.nodes.map(json => NetworkNodeInfo2.fromJSON(json)) : [];
    instance.routes = jsonObject.routes ? jsonObject.routes.map(json => NetworkRouteInfo.fromJSON(json)) : [];
    instance.networkFacts = NetworkFacts.fromJSON(jsonObject.networkFacts);
    instance.shape = NetworkShape.fromJSON(jsonObject.shape);
    return instance;
  }
}

