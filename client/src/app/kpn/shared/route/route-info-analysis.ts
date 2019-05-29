// this class is generated, please do not modify

import {List} from "immutable";
import {RouteMap} from "./route-map";
import {RouteMemberInfo} from "./route-member-info";
import {RouteNetworkNodeInfo} from "./route-network-node-info";

export class RouteInfoAnalysis {

  constructor(readonly startNodes: List<RouteNetworkNodeInfo>,
              readonly endNodes: List<RouteNetworkNodeInfo>,
              readonly startTentacleNodes: List<RouteNetworkNodeInfo>,
              readonly endTentacleNodes: List<RouteNetworkNodeInfo>,
              readonly unexpectedNodeIds: List<number>,
              readonly members: List<RouteMemberInfo>,
              readonly expectedName: string,
              readonly map: RouteMap,
              readonly structureStrings: List<string>) {
  }

  public static fromJSON(jsonObject): RouteInfoAnalysis {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteInfoAnalysis(
      jsonObject.startNodes ? List(jsonObject.startNodes.map(json => RouteNetworkNodeInfo.fromJSON(json))) : List(),
      jsonObject.endNodes ? List(jsonObject.endNodes.map(json => RouteNetworkNodeInfo.fromJSON(json))) : List(),
      jsonObject.startTentacleNodes ? List(jsonObject.startTentacleNodes.map(json => RouteNetworkNodeInfo.fromJSON(json))) : List(),
      jsonObject.endTentacleNodes ? List(jsonObject.endTentacleNodes.map(json => RouteNetworkNodeInfo.fromJSON(json))) : List(),
      jsonObject.unexpectedNodeIds ? List(jsonObject.unexpectedNodeIds) : List(),
      jsonObject.members ? List(jsonObject.members.map(json => RouteMemberInfo.fromJSON(json))) : List(),
      jsonObject.expectedName,
      RouteMap.fromJSON(jsonObject.map),
      jsonObject.structureStrings ? List(jsonObject.structureStrings) : List()
    );
  }
}
