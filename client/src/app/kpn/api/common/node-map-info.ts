// this class is generated, please do not modify

import {List} from "immutable";
import {NetworkType} from "../custom/network-type";

export class NodeMapInfo {

  constructor(readonly id: number,
              readonly name: string,
              readonly networkTypes: List<NetworkType>,
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
      jsonObject.networkTypes ? List(jsonObject.networkTypes.map((json: any) => NetworkType.fromJSON(json))) : List(),
      jsonObject.latitude,
      jsonObject.longitude
    );
  }
}
