// this file is generated, please do not modify

import { Bounds } from '@api/common/bounds';
import { RouteType } from '@api/common/route-type';

export interface RouteInfo {
  readonly routeId: number;
  readonly routeName: string;
  readonly routeTypes: RouteType[];
  readonly memberCount: number;
  readonly pathCount: number;
  readonly segmentCount: number;
  readonly changeCount: number;
  readonly bounds?: Bounds;
}
