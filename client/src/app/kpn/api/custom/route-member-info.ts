// generated, but then manually altered to avoid problem with WayDirection

import {List} from "immutable";
import {RouteNetworkNodeInfo} from "../common/route/route-network-node-info";
import {Tags} from "./tags";
import {Timestamp} from "./timestamp";

export class RouteMemberInfo {

  constructor(readonly id: number,
              readonly memberType: string,
              readonly isWay: boolean,
              readonly nodes: List<RouteNetworkNodeInfo>,
              readonly linkName: string,
              readonly from: string,
              readonly fromNodeId: number,
              readonly to: string,
              readonly toNodeId: number,
              readonly role: string,
              readonly timestamp: Timestamp,
              readonly accessible: boolean,
              readonly length: string,
              readonly nodeCount: string,
              readonly description: string,
              readonly oneWay: string,
              readonly oneWayTags: Tags) {
  }

  public static fromJSON(jsonObject: any): RouteMemberInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteMemberInfo(
      jsonObject.id,
      jsonObject.memberType,
      jsonObject.isWay,
      jsonObject.nodes ? List(jsonObject.nodes.map((json: any) => RouteNetworkNodeInfo.fromJSON(json))) : List(),
      jsonObject.linkName,
      jsonObject.from,
      jsonObject.fromNodeId,
      jsonObject.to,
      jsonObject.toNodeId,
      jsonObject.role,
      Timestamp.fromJSON(jsonObject.timestamp),
      jsonObject.accessible,
      jsonObject.length,
      jsonObject.nodeCount,
      jsonObject.description,
      jsonObject.oneWay,
      Tags.fromJSON(jsonObject.oneWayTags)
    );
  }
}
