// this file is generated, please do not modify

import { NodeData } from './node-data';
import { NodeDataUpdate } from './node-data-update';

export interface NodeDiffs {
  readonly removed: ReadonlyArray<NodeData>;
  readonly added: ReadonlyArray<NodeData>;
  readonly updated: ReadonlyArray<NodeDataUpdate>;
}
