// this class is generated, please do not modify

import {Timestamp} from '../../custom/timestamp';

export class LocationRouteInfo {

  constructor(readonly id: number,
              readonly name: string,
              readonly meters: number,
              readonly lastUpdated: Timestamp,
              readonly broken: boolean) {
  }

  public static fromJSON(jsonObject: any): LocationRouteInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationRouteInfo(
      jsonObject.id,
      jsonObject.name,
      jsonObject.meters,
      Timestamp.fromJSON(jsonObject.lastUpdated),
      jsonObject.broken
    );
  }
}
