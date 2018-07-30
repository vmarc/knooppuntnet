// this class is generated, please do not modify

import {RawNode} from './raw/raw-node';

export class Node {

  constructor(public raw?: RawNode) {
  }

  public static fromJSON(jsonObject): Node {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new Node();
    instance.raw = RawNode.fromJSON(jsonObject.raw);
    return instance;
  }
}

