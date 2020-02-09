// this class is generated, please do not modify

import {LocationSummary} from "./location-summary";

export class LocationFactsPage {

  constructor(readonly summary: LocationSummary) {
  }

  public static fromJSON(jsonObject): LocationFactsPage {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationFactsPage(
      LocationSummary.fromJSON(jsonObject.summary)
    );
  }
}
