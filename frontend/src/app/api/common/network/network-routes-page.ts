// this file is generated, please do not modify

import { RouteType } from '@api/common/route-type';
import { SurveyDateInfo } from '@api/common/survey-date-info';
import { TimeInfo } from '@api/common/time-info';
import { NetworkRouteRow } from './network-route-row';
import { NetworkSummary } from './network-summary';

export interface NetworkRoutesPage {
  readonly timeInfo: TimeInfo;
  readonly surveyDateInfo: SurveyDateInfo;
  readonly routeType: RouteType;
  readonly summary: NetworkSummary;
  readonly routes: ReadonlyArray<NetworkRouteRow>;
}
