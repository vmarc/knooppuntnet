// this file is generated, please do not modify

import { NetworkScope } from '@api/custom';
import { NetworkType } from '@api/custom';

export interface NodeRoute {
  readonly id: number;
  readonly name: string;
  readonly networkType: NetworkType;
  readonly networkScope: NetworkScope;
  readonly locationNames: string[];
  readonly expectedRouteCount: number;
  readonly actualRouteCount: number;
}
