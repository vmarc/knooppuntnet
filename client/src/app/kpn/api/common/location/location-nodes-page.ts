// this file is generated, please do not modify

import { LocationNodeInfo } from './location-node-info';
import { LocationSummary } from './location-summary';
import { TimeInfo } from '../time-info';

export interface LocationNodesPage {
  readonly timeInfo: TimeInfo;
  readonly summary: LocationSummary;
  readonly nodeCount: number;
  readonly allNodeCount: number;
  readonly factsNodeCount: number;
  readonly surveyNodeCount: number;
  readonly nodes: LocationNodeInfo[];
}
