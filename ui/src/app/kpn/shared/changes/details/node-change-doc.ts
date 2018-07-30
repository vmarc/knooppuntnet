// this class is generated, please do not modify

import {NodeChange} from './node-change';

export class NodeChangeDoc {

  constructor(public _id?: string,
              public nodeChange?: NodeChange,
              public _rev?: string) {
  }

  public static fromJSON(jsonObject): NodeChangeDoc {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NodeChangeDoc();
    instance._id = jsonObject._id;
    instance.nodeChange = jsonObject.nodeChange;
    instance._rev = jsonObject._rev;
    return instance;
  }
}

