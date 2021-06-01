// this file is generated, please do not modify

import { Day } from '../../custom/day';
import { Timestamp } from '../../custom/timestamp';

export interface LocationRouteInfo {
  readonly id: number;
  readonly name: string;
  readonly meters: number;
  readonly lastUpdated: Timestamp;
  readonly lastSurvey: Day;
  readonly broken: boolean;
  readonly accessible: boolean;
}
