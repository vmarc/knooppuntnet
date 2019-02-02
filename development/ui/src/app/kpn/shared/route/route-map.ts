// this class is generated, please do not modify

import {List} from 'immutable';
import {MapBounds} from '../common/map-bounds';
import {RouteNetworkNodeInfo} from './route-network-node-info';
import {TrackPoint} from '../common/track-point';
import {TrackSegment} from '../common/track-segment';

export class RouteMap {

  constructor(readonly bounds: MapBounds,
              readonly forwardSegments: List<TrackSegment>,
              readonly backwardSegments: List<TrackSegment>,
              readonly unusedSegments: List<TrackSegment>,
              readonly startTentacles: List<TrackSegment>,
              readonly endTentacles: List<TrackSegment>,
              readonly forwardBreakPoint: TrackPoint,
              readonly backwardBreakPoint: TrackPoint,
              readonly startNodes: List<RouteNetworkNodeInfo>,
              readonly endNodes: List<RouteNetworkNodeInfo>,
              readonly startTentacleNodes: List<RouteNetworkNodeInfo>,
              readonly endTentacleNodes: List<RouteNetworkNodeInfo>,
              readonly redundantNodes: List<RouteNetworkNodeInfo>,
              readonly halfWayPoints: List<TrackPoint>) {
  }

  public static fromJSON(jsonObject): RouteMap {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteMap(
      MapBounds.fromJSON(jsonObject.bounds),
      jsonObject.forwardSegments ? List(jsonObject.forwardSegments.map(json => TrackSegment.fromJSON(json))) : List(),
      jsonObject.backwardSegments ? List(jsonObject.backwardSegments.map(json => TrackSegment.fromJSON(json))) : List(),
      jsonObject.unusedSegments ? List(jsonObject.unusedSegments.map(json => TrackSegment.fromJSON(json))) : List(),
      jsonObject.startTentacles ? List(jsonObject.startTentacles.map(json => TrackSegment.fromJSON(json))) : List(),
      jsonObject.endTentacles ? List(jsonObject.endTentacles.map(json => TrackSegment.fromJSON(json))) : List(),
      TrackPoint.fromJSON(jsonObject.forwardBreakPoint),
      TrackPoint.fromJSON(jsonObject.backwardBreakPoint),
      jsonObject.startNodes ? List(jsonObject.startNodes.map(json => RouteNetworkNodeInfo.fromJSON(json))) : List(),
      jsonObject.endNodes ? List(jsonObject.endNodes.map(json => RouteNetworkNodeInfo.fromJSON(json))) : List(),
      jsonObject.startTentacleNodes ? List(jsonObject.startTentacleNodes.map(json => RouteNetworkNodeInfo.fromJSON(json))) : List(),
      jsonObject.endTentacleNodes ? List(jsonObject.endTentacleNodes.map(json => RouteNetworkNodeInfo.fromJSON(json))) : List(),
      jsonObject.redundantNodes ? List(jsonObject.redundantNodes.map(json => RouteNetworkNodeInfo.fromJSON(json))) : List(),
      jsonObject.halfWayPoints ? List(jsonObject.halfWayPoints.map(json => TrackPoint.fromJSON(json))) : List()
    );
  }
}
