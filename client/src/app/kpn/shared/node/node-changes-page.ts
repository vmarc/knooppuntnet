// this class is generated, please do not modify

import {List} from "immutable";
import {NodeChangeInfo} from "./node-change-info";
import {NodeInfo} from "../node-info";

export class NodeChangesPage {

  constructor(readonly nodeInfo: NodeInfo,
              readonly changes: List<NodeChangeInfo>,
              readonly incompleteWarning: boolean,
              readonly totalCount: number) {
  }

  public static fromJSON(jsonObject): NodeChangesPage {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeChangesPage(
      NodeInfo.fromJSON(jsonObject.nodeInfo),
      jsonObject.changes ? List(jsonObject.changes.map(json => NodeChangeInfo.fromJSON(json))) : List(),
      jsonObject.incompleteWarning,
      jsonObject.totalCount
    );
  }
}
