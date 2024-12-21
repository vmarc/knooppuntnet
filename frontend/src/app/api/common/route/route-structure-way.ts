// this file is generated, please do not modify

import { Tag } from '@api/custom';
import { RouteNetworkNodeInfo } from './route-network-node-info';
import { WayDirection } from './way-direction';

export interface RouteStructureWay {
  readonly nodes: RouteNetworkNodeInfo[];
  readonly from: string;
  readonly fromNodeId: number;
  readonly to: string;
  readonly toNodeId: number;
  readonly accessible: boolean;
  readonly length: string;
  readonly nodeCount: string;
  readonly description: string;
  readonly oneWay: WayDirection;
  readonly oneWayTags: Tag[];
}
