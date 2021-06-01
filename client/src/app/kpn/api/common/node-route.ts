// this file is generated, please do not modify

import { NetworkScope } from '../custom/network-scope';
import { NetworkType } from '../custom/network-type';

export interface NodeRoute {
  readonly id: number;
  readonly name: string;
  readonly networkType: NetworkType;
  readonly networkScope: NetworkScope;
  readonly locationNames: string[];
  readonly expectedRouteCount: number;
  readonly actualRouteCount: number;
}
