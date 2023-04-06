// this file is generated, please do not modify

import { Day } from '@api/custom/day';
import { MonitorRouteRelationStructureRow } from './monitor-route-relation-structure-row';

export interface MonitorRouteDetailsPage {
  readonly groupName: string;
  readonly groupDescription: string;
  readonly routeName: string;
  readonly routeDescription: string;
  readonly relationId: number;
  readonly comment: string;
  readonly referenceType: string;
  readonly referenceDay: Day;
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
