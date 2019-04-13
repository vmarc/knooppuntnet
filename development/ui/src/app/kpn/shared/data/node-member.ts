// this class is generated, please do not modify

import {Node} from "./node";

export class NodeMember {

  constructor(readonly node: Node,
              readonly role: string) {
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
