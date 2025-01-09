// this file is generated, please do not modify

import { NetworkScope } from '@api/common';
import { RouteType } from '@api/common';
import { Ref } from '@api/common/common';

export interface NodeIntegrityDetail {
  readonly routeType: RouteType;
  readonly networkScope: NetworkScope;
  readonly expectedRouteCount: number;
  readonly routeRefs: Ref[];
}
