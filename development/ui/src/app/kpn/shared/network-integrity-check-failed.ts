// this class is generated, please do not modify

import {List} from "immutable";
import {NodeIntegrityCheck} from "./node-integrity-check";

export class NetworkIntegrityCheckFailed {

  constructor(readonly count: number,
              readonly checks: List<NodeIntegrityCheck>) {
  }

  public static fromJSON(jsonObject): NetworkIntegrityCheckFailed {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkIntegrityCheckFailed(
      jsonObject.count,
      jsonObject.checks ? List(jsonObject.checks.map(json => NodeIntegrityCheck.fromJSON(json))) : List()
    );
  }
}
