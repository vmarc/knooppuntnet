// this class is generated, please do not modify

import { NodeIntegrityCheck } from './node-integrity-check';

export class NetworkIntegrityCheckFailed {
  constructor(
    readonly count: number,
    readonly checks: Array<NodeIntegrityCheck>
  ) {}

  public static fromJSON(jsonObject: any): NetworkIntegrityCheckFailed {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkIntegrityCheckFailed(
      jsonObject.count,
      jsonObject.checks.map((json: any) => NodeIntegrityCheck.fromJSON(json))
    );
  }
}
