// this class is generated, please do not modify

import { NetworkData } from './network-data';

export class NetworkDataUpdate {
  constructor(readonly before: NetworkData, readonly after: NetworkData) {}

  static fromJSON(jsonObject: any): NetworkDataUpdate {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkDataUpdate(
      NetworkData.fromJSON(jsonObject.before),
      NetworkData.fromJSON(jsonObject.after)
    );
  }
}
