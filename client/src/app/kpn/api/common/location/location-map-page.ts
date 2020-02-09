// this class is generated, please do not modify

import {LocationSummary} from "./location-summary";

export class LocationMapPage {

  constructor(readonly summary: LocationSummary) {
  }

  public static fromJSON(jsonObject): LocationMapPage {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationMapPage(
      LocationSummary.fromJSON(jsonObject.summary)
    );
  }
}
