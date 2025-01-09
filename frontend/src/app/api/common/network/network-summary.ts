// this file is generated, please do not modify

import { NetworkScope } from '@api/common';
import { RouteType } from '@api/common';

export interface NetworkSummary {
  readonly name: string;
  readonly routeType: RouteType;
  readonly networkScope: NetworkScope;
  readonly factCount: number;
  readonly nodeCount: number;
  readonly routeCount: number;
  readonly changeCount: number;
}
