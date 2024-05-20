// this file is generated, please do not modify

import { TimeInfo } from '@api/common';
import { LocationRouteInfo } from './location-route-info';
import { LocationRouteOptions } from './location-route-options';
import { LocationSummary } from './location-summary';

export interface LocationRoutesPage {
  readonly timeInfo: TimeInfo;
  readonly summary: LocationSummary;
  readonly routeCount: number;
  readonly filter: LocationRouteOptions;
  readonly routes: LocationRouteInfo[];
}
