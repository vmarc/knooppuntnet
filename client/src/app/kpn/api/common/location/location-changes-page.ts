// this class is generated, please do not modify

import {LocationSummary} from './location-summary';

export class LocationChangesPage {

  constructor(readonly summary: LocationSummary) {
  }

  public static fromJSON(jsonObject: any): LocationChangesPage {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationChangesPage(
      LocationSummary.fromJSON(jsonObject.summary)
    );
  }
}
