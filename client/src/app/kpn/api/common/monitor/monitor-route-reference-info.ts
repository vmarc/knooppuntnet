// this file is generated, please do not modify

import { Bounds } from '../bounds';
import { Day } from '../../custom/day';
import { Timestamp } from '../../custom/timestamp';

export interface MonitorRouteReferenceInfo {
  readonly created: Timestamp;
  readonly user: string;
  readonly bounds: Bounds;
  readonly distance: number;
  readonly referenceType: string;
  readonly osmReferenceDay: Day;
  readonly segmentCount: number;
  readonly gpxFilename: string;
  readonly geometry: string;
}
