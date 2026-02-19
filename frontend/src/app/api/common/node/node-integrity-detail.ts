// this file is generated, please do not modify

import { RouteScope } from '@api/common/route-scope';
import { RouteType } from '@api/common/route-type';
import { Ref } from '@api/common/common/ref';

export interface NodeIntegrityDetail {
  readonly routeType: RouteType;
  readonly routeScope: RouteScope;
  readonly expectedRouteCount: number;
  readonly routeRefs: ReadonlyArray<Ref>;
}
