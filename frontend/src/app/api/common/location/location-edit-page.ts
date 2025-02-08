// this file is generated, please do not modify

import { Bounds } from '@api/common/bounds';
import { TimeInfo } from '@api/common/time-info';
import { LocationSummary } from './location-summary';

export interface LocationEditPage {
  readonly timeInfo: TimeInfo;
  readonly summary: LocationSummary;
  readonly tooManyNodes: boolean;
  readonly maxNodes: number;
  readonly bounds: Bounds;
  readonly nodeIds: number[];
  readonly routeIds: number[];
}
