// this class is generated, please do not modify

import {TagDiffs} from '../tag-diffs';
import {NodeIntegrityCheckDiff} from './node-integrity-check-diff';
import {NodeRouteReferenceDiffs} from './node-route-reference-diffs';

export class NetworkNodeDiff {

  constructor(readonly connection: boolean,
              readonly roleConnection: boolean,
              readonly definedInNetworkRelation: boolean,
              readonly routeReferenceDiffs: NodeRouteReferenceDiffs,
              readonly nodeIntegrityCheckDiff: NodeIntegrityCheckDiff,
              readonly tagDiffs: TagDiffs) {
  }

  public static fromJSON(jsonObject: any): NetworkNodeDiff {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkNodeDiff(
      jsonObject.connection,
      jsonObject.roleConnection,
      jsonObject.definedInNetworkRelation,
      NodeRouteReferenceDiffs.fromJSON(jsonObject.routeReferenceDiffs),
      NodeIntegrityCheckDiff.fromJSON(jsonObject.nodeIntegrityCheckDiff),
      TagDiffs.fromJSON(jsonObject.tagDiffs)
    );
  }
}
