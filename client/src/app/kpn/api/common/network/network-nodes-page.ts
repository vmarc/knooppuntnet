// this file is generated, please do not modify

import { SurveyDateInfo } from '@api/common';
import { TimeInfo } from '@api/common';
import { NetworkNodeRow } from './network-node-row';
import { NetworkSummary } from './network-summary';

export interface NetworkNodesPage {
  readonly timeInfo: TimeInfo;
  readonly surveyDateInfo: SurveyDateInfo;
  readonly summary: NetworkSummary;
  readonly nodes: NetworkNodeRow[];
}
