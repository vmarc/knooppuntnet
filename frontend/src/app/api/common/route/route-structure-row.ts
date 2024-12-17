// this file is generated, please do not modify

import { Day } from '@api/custom';
import { Tag } from '@api/custom';
import { Timestamp } from '@api/custom';
import { RouteNetworkNodeInfo } from './route-network-node-info';
import { WayDirection } from './way-direction';

export interface RouteStructureRow {
  readonly id: number;
  readonly memberType: string;
  readonly isWay: boolean;
  readonly nodes: RouteNetworkNodeInfo[];
  readonly linkName: string;
  readonly from: string;
  readonly fromNodeId: number;
  readonly to: string;
  readonly toNodeId: number;
  readonly role: string;
  readonly timestamp: Timestamp;
  readonly accessible: boolean;
  readonly length: string;
  readonly nodeCount: string;
  readonly description: string;
  readonly oneWay: WayDirection;
  readonly oneWayTags: Tag[];
  readonly level: number;
  readonly physical: boolean;
  readonly name: string;
  readonly relationId: number;
  readonly subRelationIndex: number;
  readonly survey: Day;
  readonly symbol: string;
  readonly osmSegmentCount: number;
  readonly osmDistance: number;
  readonly gaps: string;
  readonly happy: boolean;
}
