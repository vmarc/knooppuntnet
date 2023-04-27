// this file is generated, please do not modify

import { Day } from '@api/custom';
import { Timestamp } from '@api/custom';

export interface LocationRouteInfo {
  readonly rowIndex: number;
  readonly id: number;
  readonly name: string;
  readonly meters: number;
  readonly lastUpdated: Timestamp;
  readonly lastSurvey: Day;
  readonly broken: boolean;
  readonly inaccessible: boolean;
}
