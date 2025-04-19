// this file is generated, please do not modify

import { Reference } from '@api/common/common/reference';
import { NodeInfo } from '@api/common/node-info';
import { NodeIntegrity } from './node-integrity';

export interface NodeDetailsPage {
  readonly nodeInfo: NodeInfo;
  readonly mixedRouteScopes: boolean;
  readonly routeReferences: Reference[];
  readonly networkReferences: Reference[];
  readonly integrity?: NodeIntegrity;
  readonly changeCount: number;
}
