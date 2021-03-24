// this class is generated, please do not modify

import {Day} from '../../custom/day';
import {Timestamp} from '../../custom/timestamp';

export class LocationRouteInfo {

  constructor(readonly id: number,
              readonly name: string,
              readonly meters: number,
              readonly lastUpdated: Timestamp,
              readonly lastSurvey: Day,
              readonly broken: boolean,
              readonly accessible: boolean) {
  }

  static fromJSON(jsonObject: any): LocationRouteInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationRouteInfo(
      jsonObject.id,
      jsonObject.name,
      jsonObject.meters,
      Timestamp.fromJSON(jsonObject.lastUpdated),
      Day.fromJSON(jsonObject.lastSurvey),
      jsonObject.broken,
      jsonObject.accessible
    );
  }
}
