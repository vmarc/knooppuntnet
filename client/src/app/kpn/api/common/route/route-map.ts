// this class is generated, please do not modify

import {List} from "immutable";
import {MapBounds} from "../common/map-bounds";
import {RouteNetworkNodeInfo} from "./route-network-node-info";
import {TrackPath} from "../common/track-path";
import {TrackPoint} from "../common/track-point";
import {TrackSegment} from "../common/track-segment";

export class RouteMap {

  constructor(readonly bounds: MapBounds,
              readonly forwardPath: TrackPath,
              readonly backwardPath: TrackPath,
              readonly unusedSegments: List<TrackSegment>,
              readonly startTentaclePaths: List<TrackPath>,
              readonly endTentaclePaths: List<TrackPath>,
              readonly forwardBreakPoint: TrackPoint,
              readonly backwardBreakPoint: TrackPoint,
              readonly startNodes: List<RouteNetworkNodeInfo>,
              readonly endNodes: List<RouteNetworkNodeInfo>,
              readonly startTentacleNodes: List<RouteNetworkNodeInfo>,
              readonly endTentacleNodes: List<RouteNetworkNodeInfo>,
              readonly redundantNodes: List<RouteNetworkNodeInfo>,
              readonly streets: List<string>) {
  }

  public static fromJSON(jsonObject): RouteMap {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteMap(
      MapBounds.fromJSON(jsonObject.bounds),
      TrackPath.fromJSON(jsonObject.forwardPath),
      TrackPath.fromJSON(jsonObject.backwardPath),
      jsonObject.unusedSegments ? List(jsonObject.unusedSegments.map(json => TrackSegment.fromJSON(json))) : List(),
      jsonObject.startTentaclePaths ? List(jsonObject.startTentaclePaths.map(json => TrackPath.fromJSON(json))) : List(),
      jsonObject.endTentaclePaths ? List(jsonObject.endTentaclePaths.map(json => TrackPath.fromJSON(json))) : List(),
      TrackPoint.fromJSON(jsonObject.forwardBreakPoint),
      TrackPoint.fromJSON(jsonObject.backwardBreakPoint),
      jsonObject.startNodes ? List(jsonObject.startNodes.map(json => RouteNetworkNodeInfo.fromJSON(json))) : List(),
      jsonObject.endNodes ? List(jsonObject.endNodes.map(json => RouteNetworkNodeInfo.fromJSON(json))) : List(),
      jsonObject.startTentacleNodes ? List(jsonObject.startTentacleNodes.map(json => RouteNetworkNodeInfo.fromJSON(json))) : List(),
      jsonObject.endTentacleNodes ? List(jsonObject.endTentacleNodes.map(json => RouteNetworkNodeInfo.fromJSON(json))) : List(),
      jsonObject.redundantNodes ? List(jsonObject.redundantNodes.map(json => RouteNetworkNodeInfo.fromJSON(json))) : List(),
      jsonObject.streets ? List(jsonObject.streets) : List()
    );
  }
}
