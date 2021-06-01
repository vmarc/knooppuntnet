// this file is generated, please do not modify

import { Bounds } from '../bounds';
import { LocationSummary } from './location-summary';
import { TimeInfo } from '../time-info';

export interface LocationEditPage {
  readonly timeInfo: TimeInfo;
  readonly summary: LocationSummary;
  readonly tooManyNodes: boolean;
  readonly maxNodes: number;
  readonly bounds: Bounds;
  readonly nodeIds: number[];
  readonly routeIds: number[];
}
