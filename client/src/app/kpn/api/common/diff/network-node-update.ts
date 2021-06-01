// this file is generated, please do not modify

import { NetworkNodeData } from './network-node-data';
import { NetworkNodeDiff } from './network/network-node-diff';

export interface NetworkNodeUpdate {
  readonly before: NetworkNodeData;
  readonly after: NetworkNodeData;
  readonly diffs: NetworkNodeDiff;
}
