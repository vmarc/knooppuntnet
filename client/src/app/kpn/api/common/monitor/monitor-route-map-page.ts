// this file is generated, please do not modify

import { Bounds } from '../bounds';
import { MonitorRouteDeviation } from './monitor-route-deviation';
import { MonitorRouteReferenceInfo } from './monitor-route-reference-info';
import { MonitorRouteSegment } from './monitor-route-segment';

export interface MonitorRouteMapPage {
  readonly routeId: string;
  readonly relationId: number;
  readonly routeName: string;
  readonly routeDescription: string;
  readonly groupName: string;
  readonly groupDescription: string;
  readonly bounds: Bounds;
  readonly osmSegments: MonitorRouteSegment[];
  readonly matchesGeometry: string;
  readonly deviations: MonitorRouteDeviation[];
  readonly reference: MonitorRouteReferenceInfo;
}
