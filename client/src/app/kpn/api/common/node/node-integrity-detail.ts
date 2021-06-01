// this file is generated, please do not modify

import { NetworkScope } from '../../custom/network-scope';
import { NetworkType } from '../../custom/network-type';
import { Ref } from '../common/ref';

export interface NodeIntegrityDetail {
  readonly networkType: NetworkType;
  readonly networkScope: NetworkScope;
  readonly expectedRouteCount: number;
  readonly routeRefs: Ref[];
}
