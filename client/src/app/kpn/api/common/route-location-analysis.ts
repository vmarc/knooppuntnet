// this class is generated, please do not modify

import {List} from "immutable";
import {Location} from "./location/location";
import {LocationCandidate} from "./location/location-candidate";

export class RouteLocationAnalysis {

  constructor(readonly location: Location,
              readonly candidates: List<LocationCandidate>,
              readonly locationNames: List<string>) {
  }

  public static fromJSON(jsonObject: any): RouteLocationAnalysis {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteLocationAnalysis(
      Location.fromJSON(jsonObject.location),
      jsonObject.candidates ? List(jsonObject.candidates.map((json: any) => LocationCandidate.fromJSON(json))) : List(),
      jsonObject.locationNames ? List(jsonObject.locationNames) : List()
    );
  }
}
