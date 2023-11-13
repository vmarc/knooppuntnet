// this file is generated, please do not modify

import { Bounds } from '@api/common';
import { Timestamp } from '@api/custom';

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
