// this file is generated, please do not modify

import { LocationRouteInfo } from './location-route-info';
import { LocationSummary } from './location-summary';
import { TimeInfo } from '../time-info';

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
