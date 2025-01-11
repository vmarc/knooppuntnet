// this file is generated, please do not modify

import { RouteScope } from '@api/common';
import { RouteType } from '@api/common';
import { Ref } from '@api/common/common';

export interface NodeIntegrityDetail {
  readonly routeType: RouteType;
  readonly routeScope: RouteScope;
  readonly expectedRouteCount: number;
  readonly routeRefs: Ref[];
}
