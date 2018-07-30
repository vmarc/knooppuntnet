// this class is generated, please do not modify

import {NetworkData} from './network-data';

export class NetworkDataUpdate {

  constructor(public before?: NetworkData,
              public after?: NetworkData) {
  }

  public static fromJSON(jsonObject): NetworkDataUpdate {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkDataUpdate();
    instance.before = NetworkData.fromJSON(jsonObject.before);
    instance.after = NetworkData.fromJSON(jsonObject.after);
    return instance;
  }
}

