// this file is generated, please do not modify

import { SurveyDateInfo } from '@api/common/survey-date-info';
import { TimeInfo } from '@api/common/time-info';
import { NetworkNodeRow } from './network-node-row';
import { NetworkSummary } from './network-summary';

export interface NetworkNodesPage {
  readonly timeInfo: TimeInfo;
  readonly surveyDateInfo: SurveyDateInfo;
  readonly summary: NetworkSummary;
  readonly nodes: NetworkNodeRow[];
}
