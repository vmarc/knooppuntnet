// this file is generated, please do not modify

import { NetworkScope } from '@api/common';
import { RouteType } from '@api/common';

export interface NodeRoute {
  readonly id: number;
  readonly name: string;
  readonly routeType: RouteType;
  readonly networkScope: NetworkScope;
  readonly locationNames: string[];
  readonly expectedRouteCount: number;
  readonly actualRouteCount: number;
}
