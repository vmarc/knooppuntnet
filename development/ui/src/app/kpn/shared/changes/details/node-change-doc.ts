// this class is generated, please do not modify

import {NodeChange} from './node-change';

export class NodeChangeDoc {
  readonly _id: string;
  readonly nodeChange: NodeChange;
  readonly _rev: string;

  constructor(_id: string,
              nodeChange: NodeChange,
              _rev: string) {
    this._id = _id;
    this.nodeChange = nodeChange;
    this._rev = _rev;
  }

  public static fromJSON(jsonObject): NodeChangeDoc {
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
