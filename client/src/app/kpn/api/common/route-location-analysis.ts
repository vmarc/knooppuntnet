// this class is generated, please do not modify

import {Location} from './location/location';
import {LocationCandidate} from './location/location-candidate';

export class RouteLocationAnalysis {

  constructor(readonly location: Location,
              readonly candidates: Array<LocationCandidate>,
              readonly locationNames: Array<string>) {
  }

  public static fromJSON(jsonObject: any): RouteLocationAnalysis {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteLocationAnalysis(
      Location.fromJSON(jsonObject.location),
      jsonObject.candidates.map((json: any) => LocationCandidate.fromJSON(json)),
      jsonObject.locationNames
    );
  }
}
