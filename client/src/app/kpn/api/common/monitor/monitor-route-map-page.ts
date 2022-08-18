// this file is generated, please do not modify

import { Bounds } from '../bounds';
import { MonitorRouteNokSegment } from './monitor-route-nok-segment';
import { MonitorRouteReferenceInfo } from './monitor-route-reference-info';
import { MonitorRouteSegment } from './monitor-route-segment';

export interface MonitorRouteMapPage {
  readonly routeId: string;
  readonly relationId: number;
  readonly routeName: string;
  readonly groupName: string;
  readonly groupDescription: string;
  readonly bounds: Bounds;
  readonly osmSegments: MonitorRouteSegment[];
  readonly okGeometry: string;
  readonly nokSegments: MonitorRouteNokSegment[];
  readonly reference: MonitorRouteReferenceInfo;
}
