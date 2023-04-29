// this file is generated, please do not modify

import { Day } from '@api/custom';

export interface MonitorRouteRelation {
  readonly relationId: number;
  readonly name: string;
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
  readonly relations: MonitorRouteRelation[];
}