// this file is generated, please do not modify

import { Link } from '@api/common/route';
import { RouteNetworkNodeInfo } from '@api/common/route';
import { WayDirection } from '@api/common/route';
import { Tag } from '@api/custom';
import { Timestamp } from '@api/custom';

export interface RouteMemberInfoWay {
  readonly wayType: string;
  readonly nodes: RouteNetworkNodeInfo[];
  readonly from: string;
  readonly fromNodeId: number;
  readonly to: string;
  readonly toNodeId: number;
  readonly timestamp: Timestamp;
  readonly accessible: boolean;
  readonly distance: number;
  readonly nodeCount: string;
  readonly description: string;
  readonly oneWay: WayDirection;
  readonly oneWayTags: Tag[];
  readonly link: Link;
}
