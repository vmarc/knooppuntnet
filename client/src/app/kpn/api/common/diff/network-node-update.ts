// this class is generated, please do not modify

import {NetworkNodeData} from "./network-node-data";
import {NetworkNodeDiff} from "./network/network-node-diff";

export class NetworkNodeUpdate {

  constructor(readonly before: NetworkNodeData,
              readonly after: NetworkNodeData,
              readonly diffs: NetworkNodeDiff) {
  }

  public static fromJSON(jsonObject: any): NetworkNodeUpdate {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkNodeUpdate(
      NetworkNodeData.fromJSON(jsonObject.before),
      NetworkNodeData.fromJSON(jsonObject.after),
      NetworkNodeDiff.fromJSON(jsonObject.diffs)
    );
  }
}
