// this file is generated, please do not modify

import { TimeInfo } from '@api/common';
import { LocationRouteInfo } from './location-route-info';
import { LocationSummary } from './location-summary';

export interface LocationRoutesPage {
  readonly timeInfo: TimeInfo;
  readonly summary: LocationSummary;
  readonly routeCount: number;
  readonly allRouteCount: number;
  readonly factsRouteCount: number;
  readonly inaccessibleRouteCount: number;
  readonly surveyRouteCount: number;
  readonly routes: LocationRouteInfo[];
}
