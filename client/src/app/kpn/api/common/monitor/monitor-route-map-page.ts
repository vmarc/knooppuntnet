// this file is generated, please do not modify

import { Bounds } from '../bounds';
import { MonitorRouteDeviation } from './monitor-route-deviation';
import { MonitorRouteReferenceInfo } from './monitor-route-reference-info';
import { MonitorRouteSegment } from './monitor-route-segment';
import { MonitorRouteSubRelation } from './monitor-route-sub-relation';

export interface MonitorRouteMapPage {
  readonly relationId: number;
  readonly routeName: string;
  readonly routeDescription: string;
  readonly groupName: string;
  readonly groupDescription: string;
  readonly bounds: Bounds;
  readonly prevSubRelation: MonitorRouteSubRelation;
  readonly nextSubRelation: MonitorRouteSubRelation;
  readonly osmSegments: MonitorRouteSegment[];
  readonly matchesGeoJson: string;
  readonly deviations: MonitorRouteDeviation[];
  readonly reference: MonitorRouteReferenceInfo;
  readonly subRelations: MonitorRouteSubRelation[];
}
