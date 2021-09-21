// this file is generated, please do not modify

import { NetworkNodeRow } from './network-node-row';
import { NetworkSummary } from './network-summary';
import { SurveyDateInfo } from '../survey-date-info';
import { TimeInfo } from '../time-info';

export interface NetworkNodesPage {
  readonly timeInfo: TimeInfo;
  readonly surveyDateInfo: SurveyDateInfo;
  readonly summary: NetworkSummary;
  readonly nodes: NetworkNodeRow[];
}
