// this file is generated, please do not modify

import { TagDiffs } from '@api/common/diff/tag-diffs';
import { FactDiffs } from '@api/common/diff/common/fact-diffs';
import { RouteNameDiff } from './route-name-diff';
import { RouteNodeDiff } from './route-node-diff';
import { RouteRoleDiff } from './route-role-diff';

export interface RouteDiff {
  readonly nameDiff?: RouteNameDiff;
  readonly roleDiff?: RouteRoleDiff;
  readonly factDiffs?: FactDiffs;
  readonly nodeDiffs: ReadonlyArray<RouteNodeDiff>;
  readonly memberOrderChanged: boolean;
  readonly tagDiffs?: TagDiffs;
}
