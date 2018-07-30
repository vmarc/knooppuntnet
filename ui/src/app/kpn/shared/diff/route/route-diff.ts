// this class is generated, please do not modify

import {FactDiffs} from '../common/fact-diffs';
import {RouteNameDiff} from './route-name-diff';
import {RouteNodeDiff} from './route-node-diff';
import {RouteRoleDiff} from './route-role-diff';
import {TagDiffs} from '../tag-diffs';

export class RouteDiff {

  constructor(public nameDiff?: RouteNameDiff,
              public roleDiff?: RouteRoleDiff,
              public factDiffs?: FactDiffs,
              public nodeDiffs?: Array<RouteNodeDiff>,
              public memberOrderChanged?: boolean,
              public tagDiffs?: TagDiffs) {
  }

  public static fromJSON(jsonObject): RouteDiff {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new RouteDiff();
    instance.nameDiff = jsonObject.nameDiff;
    instance.roleDiff = jsonObject.roleDiff;
    instance.factDiffs = jsonObject.factDiffs;
    instance.nodeDiffs = jsonObject.nodeDiffs ? jsonObject.nodeDiffs.map(json => RouteNodeDiff.fromJSON(json)) : [];
    instance.memberOrderChanged = jsonObject.memberOrderChanged;
    instance.tagDiffs = jsonObject.tagDiffs;
    return instance;
  }
}

