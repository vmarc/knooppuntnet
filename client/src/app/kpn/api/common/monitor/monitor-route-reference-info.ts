// this file is generated, please do not modify

import { Bounds } from '../bounds';
import { Day } from '@api/custom/day';
import { Timestamp } from '@api/custom/timestamp';

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
