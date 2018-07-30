// this class is generated, please do not modify

import {NetworkNodeData} from './network-node-data';
import {NetworkNodeDiff} from './network/network-node-diff';

export class NetworkNodeUpdate {

  constructor(public before?: NetworkNodeData,
              public after?: NetworkNodeData,
              public diffs?: NetworkNodeDiff) {
  }

  public static fromJSON(jsonObject): NetworkNodeUpdate {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkNodeUpdate();
    instance.before = jsonObject.before;
    instance.after = jsonObject.after;
    instance.diffs = jsonObject.diffs;
    return instance;
  }
}

