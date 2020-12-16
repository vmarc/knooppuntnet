// this class is generated, please do not modify

import {List} from 'immutable';
import {FactDiffs} from '../common/fact-diffs';
import {TagDiffs} from '../tag-diffs';
import {RouteNameDiff} from './route-name-diff';
import {RouteNodeDiff} from './route-node-diff';
import {RouteRoleDiff} from './route-role-diff';

export class RouteDiff {

  constructor(readonly nameDiff: RouteNameDiff,
              readonly roleDiff: RouteRoleDiff,
              readonly factDiffs: FactDiffs,
              readonly nodeDiffs: List<RouteNodeDiff>,
              readonly memberOrderChanged: boolean,
              readonly tagDiffs: TagDiffs) {
  }

  public static fromJSON(jsonObject: any): RouteDiff {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteDiff(
      RouteNameDiff.fromJSON(jsonObject.nameDiff),
      RouteRoleDiff.fromJSON(jsonObject.roleDiff),
      FactDiffs.fromJSON(jsonObject.factDiffs),
      jsonObject.nodeDiffs ? List(jsonObject.nodeDiffs.map((json: any) => RouteNodeDiff.fromJSON(json))) : List(),
      jsonObject.memberOrderChanged,
      TagDiffs.fromJSON(jsonObject.tagDiffs)
    );
  }
}
