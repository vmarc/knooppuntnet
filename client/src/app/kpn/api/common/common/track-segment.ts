// this class is generated, please do not modify

import {List} from "immutable";
import {TrackPoint} from "./track-point";
import {TrackSegmentFragment} from "./track-segment-fragment";

export class TrackSegment {

  constructor(readonly surface: string,
              readonly source: TrackPoint,
              readonly fragments: List<TrackSegmentFragment>) {
  }

  public static fromJSON(jsonObject): TrackSegment {
    if (!jsonObject) {
      return undefined;
    }
    return new TrackSegment(
      jsonObject.surface,
      TrackPoint.fromJSON(jsonObject.source),
      jsonObject.fragments ? List(jsonObject.fragments.map(json => TrackSegmentFragment.fromJSON(json))) : List()
    );
  }
}
