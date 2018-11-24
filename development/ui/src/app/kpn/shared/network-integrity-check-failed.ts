// this class is generated, please do not modify

import {List} from 'immutable';
import {NodeIntegrityCheck} from './node-integrity-check';

export class NetworkIntegrityCheckFailed {
  readonly count: number;
  readonly checks: List<NodeIntegrityCheck>;

  constructor(count: number,
              checks: List<NodeIntegrityCheck>) {
    this.count = count;
    this.checks = checks;
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
