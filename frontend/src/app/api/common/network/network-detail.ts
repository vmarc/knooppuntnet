// this file is generated, please do not modify

import { LatLonImpl } from '@api/common/lat-lon-impl';
import { Day } from '@api/custom/day';
import { Tag } from '@api/custom/tag';
import { Timestamp } from '@api/custom/timestamp';
import { Integrity } from './integrity';

export interface NetworkDetail {
  readonly km: number;
  readonly meters: number;
  readonly version: number;
  readonly changeSetId: number;
  readonly lastUpdated: Timestamp;
  readonly relationLastUpdated: Timestamp;
  readonly lastSurvey?: Day;
  readonly tags: Tag[];
  readonly brokenRouteCount: number;
  readonly brokenRoutePercentage: string;
  readonly integrity: Integrity;
  readonly inaccessibleRouteCount: number;
  readonly connectionCount: number;
  readonly center?: LatLonImpl;
}
