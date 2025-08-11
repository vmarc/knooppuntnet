// this file is generated, please do not modify

import { Fact } from '@api/common/fact';
import { Reference } from '@api/common/common/reference';
import { Day } from '@api/custom/day';
import { Timestamp } from '@api/custom/timestamp';

export interface LocationNodeInfo {
  readonly rowIndex: number;
  readonly id: number;
  readonly name: string;
  readonly longName: string;
  readonly latitude: string;
  readonly longitude: string;
  readonly lastUpdated: Timestamp;
  readonly lastSurvey?: Day;
  readonly facts: Fact[];
  readonly expectedRouteCount: string;
  readonly routeReferences: Reference[];
}
