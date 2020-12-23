// this file is generated, please do not modify

import {BoundsI} from '../bounds-i';
import {Timestamp} from '../../custom/timestamp';

export interface MonitorRouteReferenceInfo {
  readonly key: string;
  readonly created: Timestamp;
  readonly user: string;
  readonly bounds: BoundsI;
  readonly distance: number;
  readonly referenceType: string;
  readonly referenceTimestamp: Timestamp;
  readonly segmentCount: number;
  readonly filename: string;
  readonly geometry: string;
}
