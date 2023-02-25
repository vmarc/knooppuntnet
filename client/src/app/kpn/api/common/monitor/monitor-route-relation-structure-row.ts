// this file is generated, please do not modify

import { Day } from '../../custom/day';

export interface MonitorRouteRelationStructureRow {
  readonly level: number;
  readonly physical: boolean;
  readonly name: string;
  readonly relationId: number;
  readonly role: string;
  readonly survey: Day;
  readonly referenceDay: Day;
  readonly referenceFilename: string;
  readonly referenceDistance: number;
  readonly deviationDistance: number;
  readonly deviationCount: number;
  readonly osmWayCount: number;
  readonly osmDistance: number;
  readonly osmSegmentCount: number;
  readonly happy: boolean;
}
