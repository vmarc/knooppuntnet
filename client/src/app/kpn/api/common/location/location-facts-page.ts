// this class is generated, please do not modify

import {List} from "immutable";
import {LocationFact} from "./location-fact";
import {LocationSummary} from "./location-summary";

export class LocationFactsPage {

  constructor(readonly summary: LocationSummary,
              readonly locationFacts: List<LocationFact>) {
  }

  public static fromJSON(jsonObject: any): LocationFactsPage {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationFactsPage(
      LocationSummary.fromJSON(jsonObject.summary),
      jsonObject.locationFacts ? List(jsonObject.locationFacts.map((json: any) => LocationFact.fromJSON(json))) : List()
    );
  }
}
