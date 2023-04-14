// this file is generated, please do not modify

import { NetworkType } from '@api/custom';
import { NodeIntegrityCheck } from './node-integrity-check';

export interface NodeIntegrityCheckChange {
  readonly networkType: NetworkType;
  readonly before: NodeIntegrityCheck;
  readonly after: NodeIntegrityCheck;
}
