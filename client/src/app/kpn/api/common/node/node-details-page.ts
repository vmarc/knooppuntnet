// this file is generated, please do not modify

import { NodeInfo } from '../node-info';
import { NodeIntegrity } from './node-integrity';
import { Reference } from '../common/reference';

export interface NodeDetailsPage {
  readonly nodeInfo: NodeInfo;
  readonly mixedNetworkScopes: boolean;
  readonly routeReferences: Reference[];
  readonly networkReferences: Reference[];
  readonly integrity: NodeIntegrity;
  readonly changeCount: number;
}
