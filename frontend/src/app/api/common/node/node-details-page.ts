// this file is generated, please do not modify

import { NodeInfo } from '@api/common/node-info';
import { Reference } from '@api/common/common/reference';
import { NodeIntegrity } from './node-integrity';

export interface NodeDetailsPage {
  readonly nodeInfo: NodeInfo;
  readonly mixedRouteScopes: boolean;
  readonly routeReferences: ReadonlyArray<Reference>;
  readonly networkReferences: ReadonlyArray<Reference>;
  readonly integrity?: NodeIntegrity;
  readonly changeCount: number;
}
