// this class is generated, please do not modify

import {NetworkShape} from './network-shape';

export class NetworkMapInfo {
  readonly id: number;
  readonly name: string;
  readonly map: NetworkShape;

  constructor(id: number,
              name: string,
              map: NetworkShape) {
    this.id = id;
    this.name = name;
    this.map = map;
  }

  public static fromJSON(jsonObject): NetworkMapInfo {
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
