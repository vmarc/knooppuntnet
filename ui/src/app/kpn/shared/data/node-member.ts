// this class is generated, please do not modify

import {Node} from './node';

export class NodeMember {

  constructor(public node?: Node,
              public role?: string) {
  }

  public static fromJSON(jsonObject): NodeMember {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NodeMember();
    instance.node = jsonObject.node;
    instance.role = jsonObject.role;
    return instance;
  }
}

