// this file is generated, please do not modify

import { NetworkRouteRow } from './network-route-row';
import { NetworkSummary } from './network-summary';
import { NetworkType } from '../../custom/network-type';
import { SurveyDateInfo } from '../survey-date-info';
import { TimeInfo } from '../time-info';

export interface NetworkRoutesPage {
  readonly timeInfo: TimeInfo;
  readonly surveyDateInfo: SurveyDateInfo;
  readonly networkType: NetworkType;
  readonly networkSummary: NetworkSummary;
  readonly routes: NetworkRouteRow[];
}
