// this file is generated, please do not modify

import { NetworkType } from '../custom/network-type';
import { NodeIntegrityCheck } from './node-integrity-check';

export interface NodeIntegrityCheckChange {
  readonly networkType: NetworkType;
  readonly before: NodeIntegrityCheck;
  readonly after: NodeIntegrityCheck;
}
