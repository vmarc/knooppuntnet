// this file is generated, please do not modify

import { RouteScope } from '@api/common/route-scope';
import { RouteType } from '@api/common/route-type';

export interface NodeRoute {
  readonly id: number;
  readonly name: string;
  readonly routeType: RouteType;
  readonly routeScope: RouteScope;
  readonly locationNames: string[];
  readonly expectedRouteCount: number;
  readonly actualRouteCount: number;
}
