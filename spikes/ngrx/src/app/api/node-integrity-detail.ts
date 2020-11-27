import {Ref} from './ref';
import {NetworkType} from './network-type';

export interface NodeIntegrityDetail {
  readonly networkType: NetworkType;
  readonly expectedRouteCount: number;
  readonly routeRefs: Ref[];
}
