// this file is generated, please do not modify

import { NodeIntegrityCheckDiff } from './node-integrity-check-diff';
import { NodeRouteReferenceDiffs } from './node-route-reference-diffs';
import { TagDiffs } from '../tag-diffs';

export interface NetworkNodeDiff {
  readonly connection: boolean;
  readonly roleConnection: boolean;
  readonly definedInNetworkRelation: boolean;
  readonly routeReferenceDiffs: NodeRouteReferenceDiffs;
  readonly nodeIntegrityCheckDiff: NodeIntegrityCheckDiff;
  readonly tagDiffs: TagDiffs;
}
