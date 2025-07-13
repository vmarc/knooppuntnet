// this file is generated, please do not modify

import { Bounds } from '@api/common/bounds';
import { Timestamp } from '@api/custom/timestamp';
import { MonitorReferenceType } from './monitor-reference-type';

export interface MonitorReferenceInfo {
  readonly created: Timestamp;
  readonly user: string;
  readonly referenceBounds: Bounds;
  readonly referenceDistance: number;
  readonly referenceType: MonitorReferenceType;
  readonly referenceTimestamp: Timestamp;
  readonly referenceSegmentCount: number;
  readonly referenceFilename: string;
  readonly referenceGeoJson: string;
}
