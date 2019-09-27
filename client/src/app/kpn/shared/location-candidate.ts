// this class is generated, please do not modify

import {Location} from "./location";

export class LocationCandidate {

  constructor(readonly location: Location,
              readonly percentage: number) {
  }

  public static fromJSON(jsonObject): LocationCandidate {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationCandidate(
      Location.fromJSON(jsonObject.location),
      jsonObject.percentage
    );
  }
}
