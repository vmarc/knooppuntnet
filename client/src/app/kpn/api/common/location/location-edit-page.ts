// this class is generated, please do not modify

import {List} from "immutable";
import {Bounds} from "../bounds";
import {LocationSummary} from "./location-summary";
import {Ref} from "../common/ref";
import {TimeInfo} from "../time-info";

export class LocationEditPage {

  constructor(readonly timeInfo: TimeInfo,
              readonly summary: LocationSummary,
              readonly bounds: Bounds,
              readonly nodeRefs: List<Ref>,
              readonly routeRefs: List<Ref>) {
  }

  public static fromJSON(jsonObject: any): LocationEditPage {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationEditPage(
      TimeInfo.fromJSON(jsonObject.timeInfo),
      LocationSummary.fromJSON(jsonObject.summary),
      Bounds.fromJSON(jsonObject.bounds),
      jsonObject.nodeRefs ? List(jsonObject.nodeRefs.map((json: any) => Ref.fromJSON(json))) : List(),
      jsonObject.routeRefs ? List(jsonObject.routeRefs.map((json: any) => Ref.fromJSON(json))) : List()
    );
  }
}
