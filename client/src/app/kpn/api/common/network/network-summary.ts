// this file is generated, please do not modify

import { NetworkScope } from '@api/custom/network-scope';
import { NetworkType } from '@api/custom/network-type';

export interface NetworkSummary {
  readonly name: string;
  readonly networkType: NetworkType;
  readonly networkScope: NetworkScope;
  readonly factCount: number;
  readonly nodeCount: number;
  readonly routeCount: number;
  readonly changeCount: number;
}
