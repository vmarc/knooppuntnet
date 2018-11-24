// this class is generated, please do not modify

import {NetworkNodeData} from './network-node-data';
import {NetworkNodeDiff} from './network/network-node-diff';

export class NetworkNodeUpdate {
  readonly before: NetworkNodeData;
  readonly after: NetworkNodeData;
  readonly diffs: NetworkNodeDiff;

  constructor(before: NetworkNodeData,
              after: NetworkNodeData,
              diffs: NetworkNodeDiff) {
    this.before = before;
    this.after = after;
    this.diffs = diffs;
  }

  public static fromJSON(jsonObject): NetworkNodeUpdate {
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
