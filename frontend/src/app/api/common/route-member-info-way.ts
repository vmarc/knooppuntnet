// this file is generated, please do not modify

import { Link } from '@api/common/route/link';
import { RouteNetworkNodeInfo } from '@api/common/route/route-network-node-info';
import { WayDirection } from '@api/common/route/way-direction';
import { Tag } from '@api/custom/tag';
import { Timestamp } from '@api/custom/timestamp';

export interface RouteMemberInfoWay {
  readonly wayType?: string;
  readonly nodes: RouteNetworkNodeInfo[];
  readonly timestamp: Timestamp;
  readonly surface: string;
  readonly accessible: boolean;
  readonly ferry: boolean;
  readonly distance: number;
  readonly nodeCount: string;
  readonly oneWay: WayDirection;
  readonly oneWayTags: Tag[];
  readonly link: Link;
}
