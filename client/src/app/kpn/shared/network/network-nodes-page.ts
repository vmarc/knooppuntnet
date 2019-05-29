// this class is generated, please do not modify

import {List} from "immutable";
import {NetworkNodeInfo2} from "./network-node-info2";
import {NetworkSummary} from "./network-summary";
import {NetworkType} from "../network-type";
import {TimeInfo} from "../time-info";

export class NetworkNodesPage {

  constructor(readonly timeInfo: TimeInfo,
              readonly networkSummary: NetworkSummary,
              readonly networkType: NetworkType,
              readonly nodes: List<NetworkNodeInfo2>,
              readonly routeIds: List<number>) {
  }

  public static fromJSON(jsonObject): NetworkNodesPage {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkNodesPage(
      TimeInfo.fromJSON(jsonObject.timeInfo),
      NetworkSummary.fromJSON(jsonObject.networkSummary),
      NetworkType.fromJSON(jsonObject.networkType),
      jsonObject.nodes ? List(jsonObject.nodes.map(json => NetworkNodeInfo2.fromJSON(json))) : List(),
      jsonObject.routeIds ? List(jsonObject.routeIds) : List()
    );
  }
}
