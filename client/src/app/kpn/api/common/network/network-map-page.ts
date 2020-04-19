// this class is generated, please do not modify

import {List} from "immutable";
import {NetworkInfoNode} from "./network-info-node";
import {NetworkSummary} from "./network-summary";

export class NetworkMapPage {

  constructor(readonly networkSummary: NetworkSummary,
              readonly nodes: List<NetworkInfoNode>,
              readonly nodeIds: List<number>,
              readonly routeIds: List<number>) {
  }

  public static fromJSON(jsonObject: any): NetworkMapPage {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkMapPage(
      NetworkSummary.fromJSON(jsonObject.networkSummary),
      jsonObject.nodes ? List(jsonObject.nodes.map((json: any) => NetworkInfoNode.fromJSON(json))) : List(),
      jsonObject.nodeIds ? List(jsonObject.nodeIds) : List(),
      jsonObject.routeIds ? List(jsonObject.routeIds) : List()
    );
  }
}
