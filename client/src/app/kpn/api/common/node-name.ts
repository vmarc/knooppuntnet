// this file is generated, please do not modify

import { NetworkScope } from '../custom/network-scope';
import { NetworkType } from '../custom/network-type';

export interface NodeName {
  readonly networkType: NetworkType;
  readonly networkScope: NetworkScope;
  readonly name: string;
  readonly longName: string;
  readonly proposed: boolean;
}
