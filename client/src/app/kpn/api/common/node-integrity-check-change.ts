// this class is generated, please do not modify

import {NetworkType} from "../custom/network-type";
import {NodeIntegrityCheck} from "./node-integrity-check";

export class NodeIntegrityCheckChange {

  constructor(readonly networkType: NetworkType,
              readonly before: NodeIntegrityCheck,
              readonly after: NodeIntegrityCheck) {
  }

  public static fromJSON(jsonObject: any): NodeIntegrityCheckChange {
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
