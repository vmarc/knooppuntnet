// this class is generated, please do not modify

import {List} from "immutable";
import {Bounds} from "../bounds";
import {LocationSummary} from "./location-summary";
import {TimeInfo} from "../time-info";

export class LocationEditPage {

  constructor(readonly timeInfo: TimeInfo,
              readonly summary: LocationSummary,
              readonly bounds: Bounds,
              readonly nodeIds: List<number>,
              readonly routeIds: List<number>) {
  }

  public static fromJSON(jsonObject: any): LocationEditPage {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationEditPage(
      TimeInfo.fromJSON(jsonObject.timeInfo),
      LocationSummary.fromJSON(jsonObject.summary),
      Bounds.fromJSON(jsonObject.bounds),
      jsonObject.nodeIds ? List(jsonObject.nodeIds) : List(),
      jsonObject.routeIds ? List(jsonObject.routeIds) : List()
    );
  }
}
