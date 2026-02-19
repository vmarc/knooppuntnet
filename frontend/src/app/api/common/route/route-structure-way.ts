// this file is generated, please do not modify

import { Tag } from '@api/custom/tag';
import { RouteNetworkNodeInfo } from './route-network-node-info';
import { WayDirection } from './way-direction';

export interface RouteStructureWay {
  readonly wayType?: string;
  readonly nodes: ReadonlyArray<RouteNetworkNodeInfo>;
  readonly surface: string;
  readonly accessible: boolean;
  readonly nodeCount: string;
  readonly oneWay: WayDirection;
  readonly oneWayTags: ReadonlyArray<Tag>;
}
