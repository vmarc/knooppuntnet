// this class is generated, please do not modify

import {NodeIntegrityCheck} from '../../node-integrity-check';

export class NodeIntegrityCheckDiff {

  constructor(public before?: NodeIntegrityCheck,
              public after?: NodeIntegrityCheck) {
  }

  public static fromJSON(jsonObject): NodeIntegrityCheckDiff {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NodeIntegrityCheckDiff();
    instance.before = jsonObject.before;
    instance.after = jsonObject.after;
    return instance;
  }
}

