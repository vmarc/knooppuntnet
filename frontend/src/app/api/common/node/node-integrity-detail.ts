// this file is generated, please do not modify

import { NetworkType } from '@api/common';
import { Ref } from '@api/common/common';
import { NetworkScope } from '@api/custom';

export interface NodeIntegrityDetail {
  readonly networkType: NetworkType;
  readonly networkScope: NetworkScope;
  readonly expectedRouteCount: number;
  readonly routeRefs: Ref[];
}
