// this file is generated, please do not modify

import { NetworkScope } from '../../custom/network-scope';
import { NetworkType } from '../../custom/network-type';

export interface NetworkSummary {
  readonly name: string;
  readonly networkType: NetworkType;
  readonly networkScope: NetworkScope;
  readonly factCount: number;
  readonly nodeCount: number;
  readonly routeCount: number;
  readonly changeCount: number;
}
