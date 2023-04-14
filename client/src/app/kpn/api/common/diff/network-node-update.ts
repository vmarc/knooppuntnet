// this file is generated, please do not modify

import { NetworkNodeDiff } from './network';
import { NetworkNodeData } from './network-node-data';

export interface NetworkNodeUpdate {
  readonly before: NetworkNodeData;
  readonly after: NetworkNodeData;
  readonly diffs: NetworkNodeDiff;
}
