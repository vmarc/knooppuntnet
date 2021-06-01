// this file is generated, please do not modify

import { Day } from '../../custom/day';
import { Ref } from '../common/ref';
import { Timestamp } from '../../custom/timestamp';

export interface LocationNodeInfo {
  readonly id: number;
  readonly name: string;
  readonly longName: string;
  readonly latitude: string;
  readonly longitude: string;
  readonly lastUpdated: Timestamp;
  readonly lastSurvey: Day;
  readonly factCount: number;
  readonly expectedRouteCount: string;
  readonly routeReferences: Ref[];
}
