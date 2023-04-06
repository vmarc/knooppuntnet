// this file is generated, please do not modify

import { Day } from '@api/custom/day';
import { Fact } from '@api/custom/fact';
import { Reference } from '../common/reference';
import { Timestamp } from '@api/custom/timestamp';

export interface LocationNodeInfo {
  readonly rowIndex: number;
  readonly id: number;
  readonly name: string;
  readonly longName: string;
  readonly latitude: string;
  readonly longitude: string;
  readonly lastUpdated: Timestamp;
  readonly lastSurvey: Day;
  readonly facts: Fact[];
  readonly expectedRouteCount: string;
  readonly routeReferences: Reference[];
}
