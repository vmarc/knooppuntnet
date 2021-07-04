// this class is generated, please do not modify

import { NodeIntegrityCheck } from '../../node-integrity-check';

export class NodeIntegrityCheckDiff {
  constructor(
    readonly before: NodeIntegrityCheck,
    readonly after: NodeIntegrityCheck
  ) {}

  public static fromJSON(jsonObject: any): NodeIntegrityCheckDiff {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeIntegrityCheckDiff(
      NodeIntegrityCheck.fromJSON(jsonObject.before),
      NodeIntegrityCheck.fromJSON(jsonObject.after)
    );
  }
}
