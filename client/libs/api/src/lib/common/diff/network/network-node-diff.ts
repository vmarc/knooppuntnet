// this file is generated, please do not modify

import { TagDiffs } from '@api/common/diff';
import { NodeIntegrityCheckDiff } from './node-integrity-check-diff';
import { NodeRouteReferenceDiffs } from './node-route-reference-diffs';

export interface NetworkNodeDiff {
  readonly connection: boolean;
  readonly roleConnection: boolean;
  readonly definedInNetworkRelation: boolean;
  readonly routeReferenceDiffs: NodeRouteReferenceDiffs;
  readonly nodeIntegrityCheckDiff: NodeIntegrityCheckDiff;
  readonly tagDiffs: TagDiffs;
}
