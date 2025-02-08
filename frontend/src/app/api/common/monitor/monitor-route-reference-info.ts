// this file is generated, please do not modify

import { Bounds } from '@api/common/bounds';
import { Timestamp } from '@api/custom/timestamp';

export interface MonitorRouteReferenceInfo {
  readonly created: Timestamp;
  readonly user: string;
  readonly referenceBounds: Bounds;
  readonly referenceDistance: number;
  readonly referenceType: string;
  readonly referenceTimestamp: Timestamp;
  readonly referenceSegmentCount: number;
  readonly referenceFilename: string;
  readonly referenceGeoJson: string;
}
