// this file is generated, please do not modify

import { RouteType } from '@api/common';
import { SurveyDateInfo } from '@api/common';
import { TimeInfo } from '@api/common';
import { NetworkRouteRow } from './network-route-row';
import { NetworkSummary } from './network-summary';

export interface NetworkRoutesPage {
  readonly timeInfo: TimeInfo;
  readonly surveyDateInfo: SurveyDateInfo;
  readonly routeType: RouteType;
  readonly summary: NetworkSummary;
  readonly routes: NetworkRouteRow[];
}
