// this class is generated, please do not modify

import {NetworkShape} from './network-shape';

export class NetworkMapInfo {

  constructor(public id?: number,
              public name?: string,
              public map?: NetworkShape) {
  }

  public static fromJSON(jsonObject): NetworkMapInfo {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkMapInfo();
    instance.id = jsonObject.id;
    instance.name = jsonObject.name;
    instance.map = NetworkShape.fromJSON(jsonObject.map);
    return instance;
  }
}

