// this file is generated, please do not modify

import { NetworkAttributes } from '@api/common/network';
import { SubsetInfo } from './subset-info';

export interface SubsetNetworksPage {
  readonly subsetInfo: SubsetInfo;
  readonly km: number;
  readonly networkCount: number;
  readonly nodeCount: number;
  readonly routeCount: number;
  readonly brokenRouteNetworkCount: number;
  readonly brokenRouteNetworkPercentage: string;
  readonly brokenRouteCount: number;
  readonly brokenRoutePercentage: string;
  readonly inaccessibleRouteCount: number;
  readonly analysisUpdatedTime: string;
  readonly networks: NetworkAttributes[];
}
