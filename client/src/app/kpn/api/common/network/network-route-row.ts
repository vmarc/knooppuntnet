// this class is generated, please do not modify

import {Day} from "../../custom/day";
import {Timestamp} from "../../custom/timestamp";

export class NetworkRouteRow {

  constructor(readonly id: number,
              readonly name: string,
              readonly length: number,
              readonly role: string,
              readonly investigate: boolean,
              readonly accessible: boolean,
              readonly roleConnection: boolean,
              readonly lastUpdated: Timestamp,
              readonly lastSurvey: Day) {
  }

  public static fromJSON(jsonObject: any): NetworkRouteRow {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkRouteRow(
      jsonObject.id,
      jsonObject.name,
      jsonObject.length,
      jsonObject.role,
      jsonObject.investigate,
      jsonObject.accessible,
      jsonObject.roleConnection,
      Timestamp.fromJSON(jsonObject.lastUpdated),
      Day.fromJSON(jsonObject.lastSurvey)
    );
  }
}
