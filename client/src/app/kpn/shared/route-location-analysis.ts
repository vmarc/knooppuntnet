// this class is generated, please do not modify

import {List} from "immutable";
import {Location} from "./location";
import {LocationCandidate} from "./location-candidate";

export class RouteLocationAnalysis {

  constructor(readonly location: Location,
              readonly candidates: List<LocationCandidate>) {
  }

  public static fromJSON(jsonObject): RouteLocationAnalysis {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteLocationAnalysis(
      Location.fromJSON(jsonObject.location),
      jsonObject.candidates ? List(jsonObject.candidates.map(json => LocationCandidate.fromJSON(json))) : List()
    );
  }
}
