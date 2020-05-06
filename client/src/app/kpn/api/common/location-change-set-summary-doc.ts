// this class is generated, please do not modify

import {LocationChangeSetSummary} from "./location-change-set-summary";

export class LocationChangeSetSummaryDoc {

  constructor(readonly _id: string,
              readonly locationChangeSetSummary: LocationChangeSetSummary,
              readonly _rev: string) {
  }

  public static fromJSON(jsonObject: any): LocationChangeSetSummaryDoc {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationChangeSetSummaryDoc(
      jsonObject._id,
      LocationChangeSetSummary.fromJSON(jsonObject.locationChangeSetSummary),
      jsonObject._rev
    );
  }
}
