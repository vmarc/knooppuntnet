// this class is generated, please do not modify

import {RawNode} from './raw/raw-node';

export class Node {

  constructor(readonly raw: RawNode) {
  }

  public static fromJSON(jsonObject): Node {
    if (!jsonObject) {
      return undefined;
    }
    return new Node(
      RawNode.fromJSON(jsonObject.raw)
    );
  }
}
