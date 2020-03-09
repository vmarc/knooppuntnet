// this class is generated, please do not modify

import {NodeChange} from "./node-change";

export class NodeChangeDoc {

  constructor(readonly _id: string,
              readonly nodeChange: NodeChange,
              readonly _rev: string) {
  }

  public static fromJSON(jsonObject: any): NodeChangeDoc {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeChangeDoc(
      jsonObject._id,
      NodeChange.fromJSON(jsonObject.nodeChange),
      jsonObject._rev
    );
  }
}
