// this file is generated, please do not modify

import { FactDiffs } from '../common/fact-diffs';
import { RouteNameDiff } from './route-name-diff';
import { RouteNodeDiff } from './route-node-diff';
import { RouteRoleDiff } from './route-role-diff';
import { TagDiffs } from '../tag-diffs';

export interface RouteDiff {
  readonly nameDiff: RouteNameDiff;
  readonly roleDiff: RouteRoleDiff;
  readonly factDiffs: FactDiffs;
  readonly nodeDiffs: RouteNodeDiff[];
  readonly memberOrderChanged: boolean;
  readonly tagDiffs: TagDiffs;
}
