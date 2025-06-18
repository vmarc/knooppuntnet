// this file is generated, please do not modify

import { Bounds } from '@api/common/bounds';
import { RouteType } from '@api/common/route-type';
import { RouteSegment } from './route-segment';

export interface RouteSegmentData {
  readonly name: string;
  readonly routeTypes: RouteType[];
  readonly segments: RouteSegment[];
  readonly bounds?: Bounds;
}
