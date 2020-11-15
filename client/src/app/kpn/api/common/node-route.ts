// this class is generated, please do not modify

import {List} from "immutable";
import {NetworkType} from "../custom/network-type";

export class NodeRoute {

  constructor(readonly id: number,
              readonly name: string,
              readonly networkType: NetworkType,
              readonly locationNames: List<string>,
              readonly expectedRouteCount: number,
              readonly actualRouteCount: number) {
  }

  public static fromJSON(jsonObject: any): NodeRoute {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeRoute(
      jsonObject.id,
      jsonObject.name,
      NetworkType.fromJSON(jsonObject.networkType),
      jsonObject.locationNames ? List(jsonObject.locationNames) : List(),
      jsonObject.expectedRouteCount,
      jsonObject.actualRouteCount
    );
  }
}
