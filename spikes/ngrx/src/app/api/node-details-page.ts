import {NodeReferences} from './node-references';
import {NodeInfo} from './node-info';
import {NodeIntegrity} from './node-integrity';

export interface NodeDetailsPage {
  readonly nodeInfo: NodeInfo;
  readonly references: NodeReferences;
  readonly integrity: NodeIntegrity;
  readonly changeCount: number;
}
