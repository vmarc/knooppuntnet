// this file is generated, please do not modify

import { Timestamp } from '@api/custom';

export interface MonitorRouteDetail {
  readonly rowIndex: number;
  readonly name: string;
  readonly description: string;
  readonly symbol: string;
  readonly relationId: number;
  readonly referenceType: string;
  readonly referenceTimestamp: Timestamp;
  readonly referenceDistance: number;
  readonly deviationDistance: number;
  readonly deviationCount: number;
  readonly osmSegmentCount: number;
  readonly happy: boolean;
}
