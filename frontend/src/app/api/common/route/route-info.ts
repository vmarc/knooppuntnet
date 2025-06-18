// this file is generated, please do not modify

import { Bounds } from '@api/common/bounds';
import { RouteType } from '@api/common/route-type';

export interface RouteInfo {
  readonly routeId: number;
  readonly routeName: string;
  readonly routeTypes: RouteType[];
  readonly changeCount: number;
  readonly segmentCount: number;
  readonly bounds?: Bounds;
}
