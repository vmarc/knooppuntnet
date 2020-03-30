// this class is generated, please do not modify

import {NodeMapInfo} from "../node-map-info";

export class NodeMapPage {

  constructor(readonly nodeMapInfo: NodeMapInfo,
              readonly changeCount: number) {
  }

  public static fromJSON(jsonObject: any): NodeMapPage {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeMapPage(
      NodeMapInfo.fromJSON(jsonObject.nodeMapInfo),
      jsonObject.changeCount
    );
  }
}
