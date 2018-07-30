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
    instance.networkType = NetworkType.fromJSON(jsonObject.networkType);
    instance.before = NodeIntegrityCheck.fromJSON(jsonObject.before);
    instance.after = NodeIntegrityCheck.fromJSON(jsonObject.after);
    return instance;
  }
}

