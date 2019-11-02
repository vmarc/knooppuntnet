// this class is generated, please do not modify

import {List} from "immutable";
import {ChangeSetInfo} from "./change-set-info";
import {ChangeSetSummary} from "../change-set-summary";
import {KnownElements} from "../common/known-elements";
import {NetworkChangeInfo} from "./details/network-change-info";
import {NodeChangeInfo} from "../node/node-change-info";
import {RouteChangeInfo} from "../route/route-change-info";

export class ChangeSetPage {

  constructor(readonly summary: ChangeSetSummary,
              readonly changeSetInfo: ChangeSetInfo,
              readonly networkChanges: List<NetworkChangeInfo>,
              readonly routeChanges: List<RouteChangeInfo>,
              readonly nodeChanges: List<NodeChangeInfo>,
              readonly knownElements: KnownElements) {
  }

  public static fromJSON(jsonObject): ChangeSetPage {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangeSetPage(
      ChangeSetSummary.fromJSON(jsonObject.summary),
      ChangeSetInfo.fromJSON(jsonObject.changeSetInfo),
      jsonObject.networkChanges ? List(jsonObject.networkChanges.map(json => NetworkChangeInfo.fromJSON(json))) : List(),
      jsonObject.routeChanges ? List(jsonObject.routeChanges.map(json => RouteChangeInfo.fromJSON(json))) : List(),
      jsonObject.nodeChanges ? List(jsonObject.nodeChanges.map(json => NodeChangeInfo.fromJSON(json))) : List(),
      KnownElements.fromJSON(jsonObject.knownElements)
    );
  }
}
