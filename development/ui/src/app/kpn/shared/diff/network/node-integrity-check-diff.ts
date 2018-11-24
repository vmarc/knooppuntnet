// this class is generated, please do not modify

import {NodeIntegrityCheck} from '../../node-integrity-check';

export class NodeIntegrityCheckDiff {
  readonly before: NodeIntegrityCheck;
  readonly after: NodeIntegrityCheck;

  constructor(before: NodeIntegrityCheck,
              after: NodeIntegrityCheck) {
    this.before = before;
    this.after = after;
  }

  public static fromJSON(jsonObject): NodeIntegrityCheckDiff {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeIntegrityCheckDiff(
      NodeIntegrityCheck.fromJSON(jsonObject.before),
      NodeIntegrityCheck.fromJSON(jsonObject.after)
    );
  }
}
