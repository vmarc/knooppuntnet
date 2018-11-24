// generated, but then manually altered to avoid problem with WayDirection

import {List} from 'immutable';
import {RouteNetworkNodeInfo} from './route-network-node-info';
import {Tags} from '../data/tags';
import {Timestamp} from '../timestamp';

export class RouteMemberInfo {
  readonly id: number;
  readonly memberType: string;
  readonly isWay: boolean;
  readonly nodes: List<RouteNetworkNodeInfo>;
  readonly linkName: string;
  readonly from: string;
  readonly fromNodeId: number;
  readonly to: string;
  readonly toNodeId: number;
  readonly role: string;
  readonly timestamp: Timestamp;
  readonly isAccessible: boolean;
  readonly length: string;
  readonly nodeCount: string;
  readonly description: string;
  readonly oneWayTags: Tags;

  constructor(id: number,
              memberType: string,
              isWay: boolean,
              nodes: List<RouteNetworkNodeInfo>,
              linkName: string,
              from: string,
              fromNodeId: number,
              to: string,
              toNodeId: number,
              role: string,
              timestamp: Timestamp,
              isAccessible: boolean,
              length: string,
              nodeCount: string,
              description: string,
              oneWayTags: Tags) {
    this.id = id;
    this.memberType = memberType;
    this.isWay = isWay;
    this.nodes = nodes;
    this.linkName = linkName;
    this.from = from;
    this.fromNodeId = fromNodeId;
    this.to = to;
    this.toNodeId = toNodeId;
    this.role = role;
    this.timestamp = timestamp;
    this.isAccessible = isAccessible;
    this.length = length;
    this.nodeCount = nodeCount;
    this.description = description;
    this.oneWayTags = oneWayTags;
  }

  public static fromJSON(jsonObject): RouteMemberInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteMemberInfo(
      jsonObject.id,
      jsonObject.memberType,
      jsonObject.isWay,
      jsonObject.nodes ? List(jsonObject.nodes.map(json => RouteNetworkNodeInfo.fromJSON(json))) : List(),
      jsonObject.linkName,
      jsonObject.from,
      jsonObject.fromNodeId,
      jsonObject.to,
      jsonObject.toNodeId,
      jsonObject.role,
      Timestamp.fromJSON(jsonObject.timestamp),
      jsonObject.isAccessible,
      jsonObject.length,
      jsonObject.nodeCount,
      jsonObject.description,
      Tags.fromJSON(jsonObject.oneWayTags)
    );
  }
}
