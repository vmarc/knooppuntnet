// this class is generated, please do not modify

import {NodeIntegrityCheckDiff} from './node-integrity-check-diff';
import {NodeRouteReferenceDiffs} from './node-route-reference-diffs';
import {TagDiffs} from '../tag-diffs';

export class NetworkNodeDiff {

  constructor(public connection?: boolean,
              public definedInNetworkRelation?: boolean,
              public routeReferenceDiffs?: NodeRouteReferenceDiffs,
              public nodeIntegrityCheckDiff?: NodeIntegrityCheckDiff,
              public tagDiffs?: TagDiffs) {
  }

  public static fromJSON(jsonObject): NetworkNodeDiff {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkNodeDiff();
    instance.connection = jsonObject.connection;
    instance.definedInNetworkRelation = jsonObject.definedInNetworkRelation;
    instance.routeReferenceDiffs = NodeRouteReferenceDiffs.fromJSON(jsonObject.routeReferenceDiffs);
    instance.nodeIntegrityCheckDiff = NodeIntegrityCheckDiff.fromJSON(jsonObject.nodeIntegrityCheckDiff);
    instance.tagDiffs = TagDiffs.fromJSON(jsonObject.tagDiffs);
    return instance;
  }
}

