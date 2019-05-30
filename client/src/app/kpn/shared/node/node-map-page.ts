// this class is generated, please do not modify

import {NodeInfo} from "../node-info";

export class NodeMapPage {

  constructor(readonly nodeInfo: NodeInfo) {
  }

  public static fromJSON(jsonObject): NodeMapPage {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeMapPage(
      NodeInfo.fromJSON(jsonObject.nodeInfo)
    );
  }
}
