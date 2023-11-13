// this file is generated, please do not modify

import { Reference } from '@api/common/common';
import { Day } from '@api/custom';
import { Fact } from '@api/custom';
import { Timestamp } from '@api/custom';

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
