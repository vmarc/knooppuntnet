// this file is generated, please do not modify

import { NetworkFacts } from '@api/common';
import { NetworkInfoNode } from './network-info-node';
import { NetworkInfoRoute } from './network-info-route';
import { NetworkShape } from './network-shape';

export interface NetworkInfoDetail {
  readonly nodes: NetworkInfoNode[];
  readonly routes: NetworkInfoRoute[];
  readonly networkFacts: NetworkFacts;
  readonly shape: NetworkShape;
}
