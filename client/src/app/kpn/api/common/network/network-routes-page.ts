// this file is generated, please do not modify

import { SurveyDateInfo } from '@api/common';
import { TimeInfo } from '@api/common';
import { NetworkType } from '@api/custom';
import { NetworkRouteRow } from './network-route-row';
import { NetworkSummary } from './network-summary';

export interface NetworkRoutesPage {
  readonly timeInfo: TimeInfo;
  readonly surveyDateInfo: SurveyDateInfo;
  readonly networkType: NetworkType;
  readonly summary: NetworkSummary;
  readonly routes: NetworkRouteRow[];
}
