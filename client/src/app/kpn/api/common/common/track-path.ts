// this class is generated, please do not modify

import {List} from "immutable";
import {TrackSegment} from "./track-segment";

export class TrackPath {

  constructor(readonly startNodeId: number,
              readonly endNodeId: number,
              readonly meters: number,
              readonly segments: List<TrackSegment>) {
  }

  public static fromJSON(jsonObject: any): TrackPath {
    if (!jsonObject) {
      return undefined;
    }
    return new TrackPath(
      jsonObject.startNodeId,
      jsonObject.endNodeId,
      jsonObject.meters,
      jsonObject.segments ? List(jsonObject.segments.map((json: any) => TrackSegment.fromJSON(json))) : List()
    );
  }
}
