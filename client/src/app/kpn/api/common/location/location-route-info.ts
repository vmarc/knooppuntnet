// this class is generated, please do not modify

import {Timestamp} from "../../custom/timestamp";

export class LocationRouteInfo {

  constructor(readonly id: number,
              readonly name: string,
              readonly length: number,
              readonly investigate: boolean,
              readonly accessible: boolean,
              readonly relationLastUpdated: Timestamp) {
  }

  public static fromJSON(jsonObject): LocationRouteInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationRouteInfo(
      jsonObject.id,
      jsonObject.name,
      jsonObject.length,
      jsonObject.investigate,
      jsonObject.accessible,
      Timestamp.fromJSON(jsonObject.relationLastUpdated)
    );
  }
}
