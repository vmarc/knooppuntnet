// this file is generated, please do not modify

import { Day } from '@api/custom';
import { Timestamp } from '@api/custom';

export interface MonitorRouteRelationStructureRow {
  readonly level: number;
  readonly physical: boolean;
  readonly name: string;
  readonly relationId: number;
  readonly role: string;
  readonly survey: Day;
  readonly symbol: string;
  readonly referenceTimestamp: Timestamp;
  readonly referenceFilename: string;
  readonly referenceDistance: number;
  readonly deviationDistance: number;
  readonly deviationCount: number;
  readonly osmWayCount: number;
  readonly osmSegmentCount: number;
  readonly osmDistance: number;
  readonly osmDistanceSubRelations: number;
  readonly gaps: string;
  readonly happy: boolean;
}
