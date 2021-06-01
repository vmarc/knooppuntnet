// this file is generated, please do not modify

import { NetworkNodeDetail } from './network-node-detail';
import { NetworkScope } from '../../custom/network-scope';
import { NetworkSummary } from './network-summary';
import { NetworkType } from '../../custom/network-type';
import { SurveyDateInfo } from '../survey-date-info';
import { TimeInfo } from '../time-info';

export interface NetworkNodesPage {
  readonly timeInfo: TimeInfo;
  readonly surveyDateInfo: SurveyDateInfo;
  readonly networkSummary: NetworkSummary;
  readonly networkType: NetworkType;
  readonly networkScope: NetworkScope;
  readonly nodes: NetworkNodeDetail[];
  readonly routeIds: number[];
}
