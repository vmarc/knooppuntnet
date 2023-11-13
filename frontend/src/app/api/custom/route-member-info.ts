import { RouteNetworkNodeInfo } from '../common/route';
import { Tags } from './tags';
import { Timestamp } from './timestamp';

export interface RouteMemberInfo {
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
  readonly oneWay: string;
  readonly oneWayTags: Tags;
}
