// this class is generated, please do not modify

import {Node} from './node';

export class NodeMember {
  readonly node: Node;
  readonly role: string;

  constructor(node: Node,
              role: string) {
    this.node = node;
    this.role = role;
  }

  public static fromJSON(jsonObject): NodeMember {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeMember(
      Node.fromJSON(jsonObject.node),
      jsonObject.role
    );
  }
}
