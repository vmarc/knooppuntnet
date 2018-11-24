// this class is generated, please do not modify

import {NetworkData} from './network-data';

export class NetworkDataUpdate {
  readonly before: NetworkData;
  readonly after: NetworkData;

  constructor(before: NetworkData,
              after: NetworkData) {
    this.before = before;
    this.after = after;
  }

  public static fromJSON(jsonObject): NetworkDataUpdate {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkDataUpdate(
      NetworkData.fromJSON(jsonObject.before),
      NetworkData.fromJSON(jsonObject.after)
    );
  }
}
