// this class is generated, please do not modify

import {NetworkType} from '../custom/network-type';

export class NodeMapInfo {

  constructor(readonly id: number,
              readonly name: string,
              readonly networkTypes: Array<NetworkType>,
              readonly latitude: string,
              readonly longitude: string) {
  }

  public static fromJSON(jsonObject: any): NodeMapInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeMapInfo(
      jsonObject.id,
      jsonObject.name,
      jsonObject.networkTypes.map((json: any) => NetworkType.fromJSON(json)),
      jsonObject.latitude,
      jsonObject.longitude
    );
  }
}
