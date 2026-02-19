// this file is generated, please do not modify

import { TimeInfo } from '@api/common/time-info';
import { LocationNodeInfo } from './location-node-info';
import { LocationNodeOptions } from './location-node-options';
import { LocationSummary } from './location-summary';

export interface LocationNodesPage {
  readonly timeInfo: TimeInfo;
  readonly summary: LocationSummary;
  readonly nodeCount: number;
  readonly filter: LocationNodeOptions;
  readonly nodes: ReadonlyArray<LocationNodeInfo>;
}
