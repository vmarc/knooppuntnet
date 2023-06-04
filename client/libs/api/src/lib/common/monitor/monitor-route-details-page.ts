// this file is generated, please do not modify

import { Timestamp } from '@api/custom';
import { MonitorRouteRelationStructureRow } from './monitor-route-relation-structure-row';

export interface MonitorRouteDetailsPage {
  readonly adminRole: boolean;
  readonly groupName: string;
  readonly groupDescription: string;
  readonly routeName: string;
  readonly routeDescription: string;
  readonly relationId: number;
  readonly comment: string;
  readonly referenceType: string;
  readonly referenceTimestamp: Timestamp;
  readonly referenceFilename: string;
  readonly referenceDistance: number;
  readonly deviationDistance: number;
  readonly deviationCount: number;
  readonly osmSegmentCount: number;
  readonly happy: boolean;
  readonly wayCount: number;
  readonly osmDistance: number;
  readonly structureRows: MonitorRouteRelationStructureRow[];
}
