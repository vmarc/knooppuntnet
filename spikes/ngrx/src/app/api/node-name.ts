import {ScopedNetworkType} from './scoped-network-type';

export interface NodeName {
  readonly scopedNetworkType: ScopedNetworkType;
  readonly name: string;
}
