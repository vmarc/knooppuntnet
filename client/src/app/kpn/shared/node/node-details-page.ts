// this class is generated, please do not modify

import {NodeInfo} from "../node-info";
import {NodeReferences} from "./node-references";

export class NodeDetailsPage {

  constructor(readonly nodeInfo: NodeInfo,
              readonly references: NodeReferences) {
  }

  public static fromJSON(jsonObject): NodeDetailsPage {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeDetailsPage(
      NodeInfo.fromJSON(jsonObject.nodeInfo),
      NodeReferences.fromJSON(jsonObject.references)
    );
  }
}
