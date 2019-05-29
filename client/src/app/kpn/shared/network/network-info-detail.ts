// this class is generated, please do not modify

import {List} from "immutable";
import {NetworkFacts} from "../network-facts";
import {NetworkNodeInfo2} from "./network-node-info2";
import {NetworkRouteInfo} from "./network-route-info";
import {NetworkShape} from "./network-shape";

export class NetworkInfoDetail {

  constructor(readonly nodes: List<NetworkNodeInfo2>,
              readonly routes: List<NetworkRouteInfo>,
              readonly networkFacts: NetworkFacts,
              readonly shape: NetworkShape) {
  }

  public static fromJSON(jsonObject): NetworkInfoDetail {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkInfoDetail(
      jsonObject.nodes ? List(jsonObject.nodes.map(json => NetworkNodeInfo2.fromJSON(json))) : List(),
      jsonObject.routes ? List(jsonObject.routes.map(json => NetworkRouteInfo.fromJSON(json))) : List(),
      NetworkFacts.fromJSON(jsonObject.networkFacts),
      NetworkShape.fromJSON(jsonObject.shape)
    );
  }
}
