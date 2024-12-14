// this file is generated, please do not modify

import { NetworkScope } from '@api/common';
import { NetworkType } from '@api/common';
import { Ref } from '@api/common/common';

export interface NodeIntegrityDetail {
  readonly networkType: NetworkType;
  readonly networkScope: NetworkScope;
  readonly expectedRouteCount: number;
  readonly routeRefs: Ref[];
}
