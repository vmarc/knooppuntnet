// this file is generated, please do not modify

import { Bounds } from '@api/common';

export interface MonitorRouteSegment {
  readonly id: number;
  readonly startNodeId: number;
  readonly endNodeId: number;
  readonly meters: number;
  readonly bounds: Bounds;
  readonly geoJson: string;
}
