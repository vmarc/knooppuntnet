// this file is generated, please do not modify

import { MapBounds } from '@api/common/common';
import { TrackPath } from '@api/common/common';
import { TrackPoint } from '@api/common/common';
import { TrackSegment } from '@api/common/common';
import { RouteNetworkNodeInfo } from './route-network-node-info';

export interface RouteMap {
  readonly bounds: MapBounds;
  readonly freePaths: TrackPath[];
  readonly forwardPath: TrackPath;
  readonly backwardPath: TrackPath;
  readonly unusedSegments: TrackSegment[];
  readonly startTentaclePaths: TrackPath[];
  readonly endTentaclePaths: TrackPath[];
  readonly forwardBreakPoint: TrackPoint;
  readonly backwardBreakPoint: TrackPoint;
  readonly freeNodes: RouteNetworkNodeInfo[];
  readonly startNodes: RouteNetworkNodeInfo[];
  readonly endNodes: RouteNetworkNodeInfo[];
  readonly startTentacleNodes: RouteNetworkNodeInfo[];
  readonly endTentacleNodes: RouteNetworkNodeInfo[];
  readonly redundantNodes: RouteNetworkNodeInfo[];
  readonly streets: string[];
}
