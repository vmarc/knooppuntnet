// this class is generated, please do not modify

import {NodeIntegrityCheck} from './node-integrity-check';

export class NetworkIntegrityCheckFailed {

  constructor(public count?: number,
              public checks?: Array<NodeIntegrityCheck>) {
  }

  public static fromJSON(jsonObject): NetworkIntegrityCheckFailed {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkIntegrityCheckFailed();
    instance.count = jsonObject.count;
    instance.checks = jsonObject.checks ? jsonObject.checks.map(json => NodeIntegrityCheck.fromJSON(json)) : [];
    return instance;
  }
}

