// this class is generated, please do not modify

import {List} from "immutable";
import {LocationRouteInfo} from "./location-route-info";
import {LocationSummary} from "./location-summary";
import {TimeInfo} from "../time-info";

export class LocationRoutesPage {

  constructor(readonly timeInfo: TimeInfo,
              readonly summary: LocationSummary,
              readonly routes: List<LocationRouteInfo>) {
  }

  public static fromJSON(jsonObject): LocationRoutesPage {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationRoutesPage(
      TimeInfo.fromJSON(jsonObject.timeInfo),
      LocationSummary.fromJSON(jsonObject.summary),
      jsonObject.routes ? List(jsonObject.routes.map(json => LocationRouteInfo.fromJSON(json))) : List()
    );
  }
}
