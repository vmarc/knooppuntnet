// this class is generated, please do not modify

import {NetworkShape} from './network-shape';

export class NetworkMapInfo {

  constructor(readonly id: number,
              readonly name: string,
              readonly map: NetworkShape) {
  }

  public static fromJSON(jsonObject: any): NetworkMapInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkMapInfo(
      jsonObject.id,
      jsonObject.name,
      NetworkShape.fromJSON(jsonObject.map)
    );
  }
}
