// this class is generated, please do not modify

import {List} from "immutable";
import {NodeChangeInfo} from "./node-change-info";

export class NodeChangeInfos {

  constructor(readonly changes: List<NodeChangeInfo>,
              readonly incompleteWarning: boolean,
              readonly more: boolean) {
  }

  public static fromJSON(jsonObject): NodeChangeInfos {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeChangeInfos(
      jsonObject.changes ? List(jsonObject.changes.map(json => NodeChangeInfo.fromJSON(json))) : List(),
      jsonObject.incompleteWarning,
      jsonObject.more
    );
  }
}
