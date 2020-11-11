// this class is generated, please do not modify

import {List} from "immutable";
import {Ref} from "../common/ref";
import {Timestamp} from "../../custom/timestamp";

export class LocationNodeInfo {

  constructor(readonly id: number,
              readonly name: string,
              readonly latitude: string,
              readonly longitude: string,
              readonly lastUpdated: Timestamp,
              readonly factCount: number,
              readonly expectedRouteCount: number,
              readonly routeReferences: List<Ref>) {
  }

  public static fromJSON(jsonObject: any): LocationNodeInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationNodeInfo(
      jsonObject.id,
      jsonObject.name,
      jsonObject.latitude,
      jsonObject.longitude,
      Timestamp.fromJSON(jsonObject.lastUpdated),
      jsonObject.factCount,
      jsonObject.expectedRouteCount,
      jsonObject.routeReferences ? List(jsonObject.routeReferences.map((json: any) => Ref.fromJSON(json))) : List()
    );
  }
}
