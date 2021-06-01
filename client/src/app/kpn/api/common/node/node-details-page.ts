// this file is generated, please do not modify

import { NodeInfo } from '../node-info';
import { NodeIntegrity } from './node-integrity';
import { NodeReferences } from './node-references';

export interface NodeDetailsPage {
  readonly nodeInfo: NodeInfo;
  readonly references: NodeReferences;
  readonly integrity: NodeIntegrity;
  readonly changeCount: number;
}
