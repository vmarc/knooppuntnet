// this class is generated, please do not modify

import {NetworkData} from './network-data';

export class NetworkDataUpdate {

  constructor(readonly before: NetworkData,
              readonly after: NetworkData) {
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
