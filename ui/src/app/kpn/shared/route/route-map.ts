// this class is generated, please do not modify

import {MapBounds} from '../common/map-bounds';
import {RouteNetworkNodeInfo} from './route-network-node-info';
import {TrackPoint} from '../common/track-point';
import {TrackSegment} from '../common/track-segment';

export class RouteMap {

  constructor(public bounds?: MapBounds,
              public forwardSegments?: Array<TrackSegment>,
              public backwardSegments?: Array<TrackSegment>,
              public unusedSegments?: Array<TrackSegment>,
              public startTentacles?: Array<TrackSegment>,
              public endTentacles?: Array<TrackSegment>,
              public forwardBreakPoint?: TrackPoint,
              public backwardBreakPoint?: TrackPoint,
              public startNodes?: Array<RouteNetworkNodeInfo>,
              public endNodes?: Array<RouteNetworkNodeInfo>,
              public startTentacleNodes?: Array<RouteNetworkNodeInfo>,
              public endTentacleNodes?: Array<RouteNetworkNodeInfo>,
              public redundantNodes?: Array<RouteNetworkNodeInfo>,
              public halfWayPoints?: Array<TrackPoint>) {
  }

  public static fromJSON(jsonObject): RouteMap {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new RouteMap();
    instance.bounds = jsonObject.bounds;
    instance.forwardSegments = jsonObject.forwardSegments ? jsonObject.forwardSegments.map(json => TrackSegment.fromJSON(json)) : [];
    instance.backwardSegments = jsonObject.backwardSegments ? jsonObject.backwardSegments.map(json => TrackSegment.fromJSON(json)) : [];
    instance.unusedSegments = jsonObject.unusedSegments ? jsonObject.unusedSegments.map(json => TrackSegment.fromJSON(json)) : [];
    instance.startTentacles = jsonObject.startTentacles ? jsonObject.startTentacles.map(json => TrackSegment.fromJSON(json)) : [];
    instance.endTentacles = jsonObject.endTentacles ? jsonObject.endTentacles.map(json => TrackSegment.fromJSON(json)) : [];
    instance.forwardBreakPoint = jsonObject.forwardBreakPoint;
    instance.backwardBreakPoint = jsonObject.backwardBreakPoint;
    instance.startNodes = jsonObject.startNodes ? jsonObject.startNodes.map(json => RouteNetworkNodeInfo.fromJSON(json)) : [];
    instance.endNodes = jsonObject.endNodes ? jsonObject.endNodes.map(json => RouteNetworkNodeInfo.fromJSON(json)) : [];
    instance.startTentacleNodes = jsonObject.startTentacleNodes ? jsonObject.startTentacleNodes.map(json => RouteNetworkNodeInfo.fromJSON(json)) : [];
    instance.endTentacleNodes = jsonObject.endTentacleNodes ? jsonObject.endTentacleNodes.map(json => RouteNetworkNodeInfo.fromJSON(json)) : [];
    instance.redundantNodes = jsonObject.redundantNodes ? jsonObject.redundantNodes.map(json => RouteNetworkNodeInfo.fromJSON(json)) : [];
    instance.halfWayPoints = jsonObject.halfWayPoints ? jsonObject.halfWayPoints.map(json => TrackPoint.fromJSON(json)) : [];
    return instance;
  }
}

