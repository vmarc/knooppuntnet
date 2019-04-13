// this class is generated, please do not modify

import {NodeInfo} from "../node-info";
import {NodeReferences} from "./node-references";

export class MapDetailNode {

  constructor(readonly info: NodeInfo,
              readonly references: NodeReferences) {
  }

  public static fromJSON(jsonObject): MapDetailNode {
    if (!jsonObject) {
      return undefined;
    }
    return new MapDetailNode(
      NodeInfo.fromJSON(jsonObject.info),
      NodeReferences.fromJSON(jsonObject.references)
    );
  }
}
