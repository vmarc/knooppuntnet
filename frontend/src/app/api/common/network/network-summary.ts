// this file is generated, please do not modify

import { RouteScope } from '@api/common';
import { RouteType } from '@api/common';

export interface NetworkSummary {
  readonly name: string;
  readonly routeType: RouteType;
  readonly routeScope: RouteScope;
  readonly factCount: number;
  readonly nodeCount: number;
  readonly routeCount: number;
  readonly changeCount: number;
}
