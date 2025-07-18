// this file is generated, please do not modify

import { Bounds } from '@api/common/bounds';
import { Timestamp } from '@api/custom/timestamp';

export interface MonitorRouteDetail {
  readonly rowIndex: number;
  readonly routeId: string;
  readonly name: string;
  readonly description: string;
  readonly symbol: string;
  readonly relationId: number;
  readonly referenceType: string;
  readonly referenceTimestamp?: Timestamp;
  readonly referenceDistance: number;
  readonly deviationDistance: number;
  readonly deviationCount: number;
  readonly osmSegmentCount: number;
  readonly relationIds: number[];
  readonly bounds?: Bounds;
  readonly happy: boolean;
}
