// this class is generated, please do not modify

import {List} from "immutable";
import {Bounds} from "../bounds";
import {Ids} from "./ids";
import {LocationSummary} from "./location-summary";
import {TimeInfo} from "../time-info";

export class LocationSummaryPage {

  constructor(readonly timeInfo: TimeInfo,
              readonly summary: LocationSummary,
              readonly bounds: Bounds,
              readonly nodeIds: List<Ids>,
              readonly routeIds: List<Ids>) {
  }

  public static fromJSON(jsonObject: any): LocationSummaryPage {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationSummaryPage(
      TimeInfo.fromJSON(jsonObject.timeInfo),
      LocationSummary.fromJSON(jsonObject.summary),
      Bounds.fromJSON(jsonObject.bounds),
      jsonObject.nodeIds ? List(jsonObject.nodeIds.map((json: any) => Ids.fromJSON(json))) : List(),
      jsonObject.routeIds ? List(jsonObject.routeIds.map((json: any) => Ids.fromJSON(json))) : List()
    );
  }
}
