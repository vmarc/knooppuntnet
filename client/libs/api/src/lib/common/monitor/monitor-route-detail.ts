// this file is generated, please do not modify

import { Day } from '@api/custom';

export interface MonitorRouteDetail {
  readonly rowIndex: number;
  readonly routeId: string;
  readonly name: string;
  readonly description: string;
  readonly relationId: number;
  readonly referenceType: string;
  readonly referenceDay: Day;
  readonly referenceDistance: number;
  readonly deviationDistance: number;
  readonly deviationCount: number;
  readonly osmSegmentCount: number;
  readonly happy: boolean;
}
