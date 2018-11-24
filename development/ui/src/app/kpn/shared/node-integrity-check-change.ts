// this class is generated, please do not modify

import {NetworkType} from './network-type';
import {NodeIntegrityCheck} from './node-integrity-check';

export class NodeIntegrityCheckChange {
  readonly networkType: NetworkType;
  readonly before: NodeIntegrityCheck;
  readonly after: NodeIntegrityCheck;

  constructor(networkType: NetworkType,
              before: NodeIntegrityCheck,
              after: NodeIntegrityCheck) {
    this.networkType = networkType;
    this.before = before;
    this.after = after;
  }

  public static fromJSON(jsonObject): NodeIntegrityCheckChange {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeIntegrityCheckChange(
      NetworkType.fromJSON(jsonObject.networkType),
      NodeIntegrityCheck.fromJSON(jsonObject.before),
      NodeIntegrityCheck.fromJSON(jsonObject.after)
    );
  }
}
