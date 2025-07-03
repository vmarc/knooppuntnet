// this file is generated, please do not modify

import { StructureRow } from '@api/common/route/structure-row';
import { Timestamp } from '@api/custom/timestamp';
import { MonitorReferenceType } from './monitor-reference-type';

export interface MonitorRouteDetailsPage {
  readonly adminRole: boolean;
  readonly groupName: string;
  readonly groupDescription: string;
  readonly routeName: string;
  readonly routeDescription: string;
  readonly relationId: number;
  readonly comment: string;
  readonly symbol: string;
  readonly analysisTimestamp?: Timestamp;
  readonly analysisDuration: number;
  readonly referenceType: MonitorReferenceType;
  readonly referenceTimestamp?: Timestamp;
  readonly referenceFilename: string;
  readonly referenceDistance: number;
  readonly deviationDistance: number;
  readonly deviationCount: number;
  readonly osmSegmentCount: number;
  readonly happy: boolean;
  readonly wayCount: number;
  readonly osmDistance: number;
  readonly relationCount: number;
  readonly relationLevels: number;
  readonly structureRows: StructureRow[];
}
