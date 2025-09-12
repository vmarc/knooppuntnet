// this file is generated, please do not modify

import { Fact } from '@api/common/fact';
import { Day } from '@api/custom/day';
import { Timestamp } from '@api/custom/timestamp';

export interface LocationRouteInfo {
  readonly rowIndex: number;
  readonly id: number;
  readonly name: string;
  readonly meters: number;
  readonly lastUpdated: Timestamp;
  readonly lastSurvey?: Day;
  readonly symbol?: string;
  readonly broken: boolean;
  readonly proposed: boolean;
  readonly facts: Fact[];
}
