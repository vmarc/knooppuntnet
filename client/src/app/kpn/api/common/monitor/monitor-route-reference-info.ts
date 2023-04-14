// this file is generated, please do not modify

import { Bounds } from '@api/common';
import { Day } from '@api/custom';
import { Timestamp } from '@api/custom';

export interface MonitorRouteReferenceInfo {
  readonly created: Timestamp;
  readonly user: string;
  readonly bounds: Bounds;
  readonly distance: number;
  readonly referenceType: string;
  readonly referenceDay: Day;
  readonly segmentCount: number;
  readonly gpxFilename: string;
  readonly geoJson: string;
}
