// this file is generated, please do not modify

import { Day } from '../../custom/day';
import { Integrity } from './integrity';
import { LatLonImpl } from '../lat-lon-impl';
import { Tags } from '../../custom/tags';
import { Timestamp } from '../../custom/timestamp';

export interface NetworkDetail {
  readonly km: number;
  readonly meters: number;
  readonly lastUpdated: Timestamp;
  readonly relationLastUpdated: Timestamp;
  readonly lastSurvey: Day;
  readonly tags: Tags;
  readonly brokenRouteCount: number;
  readonly brokenRoutePercentage: string;
  readonly integrity: Integrity;
  readonly unaccessibleRouteCount: number;
  readonly connectionCount: number;
  readonly center: LatLonImpl;
}
