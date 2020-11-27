import {NetworkType} from './network-type';

export interface ScopedNetworkType {
  readonly networkScope: string;
  readonly networkType: NetworkType;
  readonly key: string;
}
