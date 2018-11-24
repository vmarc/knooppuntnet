// this class is generated, please do not modify

import {RawNode} from './raw/raw-node';

export class Node {
  readonly raw: RawNode;

  constructor(raw: RawNode) {
    this.raw = raw;
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
