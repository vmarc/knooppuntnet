// this class is generated, please do not modify

import {RawNode} from "../data/raw/raw-node";

export class NetworkNodeData {

  constructor(readonly node: RawNode,
              readonly name: string) {
  }

  public static fromJSON(jsonObject: any): NetworkNodeData {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkNodeData(
      RawNode.fromJSON(jsonObject.node),
      jsonObject.name
    );
  }
}
