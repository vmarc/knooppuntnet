// this class is generated, please do not modify

import {MapBounds} from '../common/map-bounds';
import {TrackPath} from '../common/track-path';
import {TrackPoint} from '../common/track-point';
import {TrackSegment} from '../common/track-segment';
import {RouteNetworkNodeInfo} from './route-network-node-info';

export class RouteMap {

  constructor(readonly bounds: MapBounds,
              readonly forwardPath: TrackPath,
              readonly backwardPath: TrackPath,
              readonly unusedSegments: Array<TrackSegment>,
              readonly startTentaclePaths: Array<TrackPath>,
              readonly endTentaclePaths: Array<TrackPath>,
              readonly forwardBreakPoint: TrackPoint,
              readonly backwardBreakPoint: TrackPoint,
              readonly startNodes: Array<RouteNetworkNodeInfo>,
              readonly endNodes: Array<RouteNetworkNodeInfo>,
              readonly startTentacleNodes: Array<RouteNetworkNodeInfo>,
              readonly endTentacleNodes: Array<RouteNetworkNodeInfo>,
              readonly redundantNodes: Array<RouteNetworkNodeInfo>,
              readonly streets: Array<string>,
              readonly trackPaths: Array<TrackPath>) {
  }

  static fromJSON(jsonObject: any): RouteMap {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteMap(
      MapBounds.fromJSON(jsonObject.bounds),
      TrackPath.fromJSON(jsonObject.forwardPath),
      TrackPath.fromJSON(jsonObject.backwardPath),
      jsonObject.unusedSegments.map((json: any) => TrackSegment.fromJSON(json)),
      jsonObject.startTentaclePaths.map((json: any) => TrackPath.fromJSON(json)),
      jsonObject.endTentaclePaths.map((json: any) => TrackPath.fromJSON(json)),
      TrackPoint.fromJSON(jsonObject.forwardBreakPoint),
      TrackPoint.fromJSON(jsonObject.backwardBreakPoint),
      jsonObject.startNodes.map((json: any) => RouteNetworkNodeInfo.fromJSON(json)),
      jsonObject.endNodes.map((json: any) => RouteNetworkNodeInfo.fromJSON(json)),
      jsonObject.startTentacleNodes.map((json: any) => RouteNetworkNodeInfo.fromJSON(json)),
      jsonObject.endTentacleNodes.map((json: any) => RouteNetworkNodeInfo.fromJSON(json)),
      jsonObject.redundantNodes.map((json: any) => RouteNetworkNodeInfo.fromJSON(json)),
      jsonObject.streets,
      !!jsonObject.trackPaths ? jsonObject.trackPaths.map((json: any) => TrackPath.fromJSON(json)) : []
    );
  }
}
