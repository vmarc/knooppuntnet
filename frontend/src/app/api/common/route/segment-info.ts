// this file is generated, please do not modify

import { Bounds } from '@api/common/bounds';
import { SegmentRouteInfo } from './segment-route-info';

export interface SegmentInfo {
  readonly id: number;
  readonly meters: number;
  readonly bounds?: Bounds;
  readonly routeInfos: ReadonlyArray<SegmentRouteInfo>;
}
