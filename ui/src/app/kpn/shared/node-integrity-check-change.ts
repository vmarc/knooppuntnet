// this class is generated, please do not modify

import {NetworkType} from './network-type';
import {NodeIntegrityCheck} from './node-integrity-check';

export class NodeIntegrityCheckChange {

  constructor(public networkType?: NetworkType,
              public before?: NodeIntegrityCheck,
              public after?: NodeIntegrityCheck) {
  }

  public static fromJSON(jsonObject): NodeIntegrityCheckChange {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NodeIntegrityCheckChange();
    instance.networkType = jsonObject.networkType;
    instance.before = jsonObject.before;
    instance.after = jsonObject.after;
    return instance;
  }
}

