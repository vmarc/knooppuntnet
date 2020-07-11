// this class is generated, please do not modify

import {List} from "immutable";
import {TrackPathKey} from "../common/track-path-key";

export class LegEndRoute {

  constructor(readonly trackPathKeys: List<TrackPathKey>) {
  }

  public static fromJSON(jsonObject: any): LegEndRoute {
    if (!jsonObject) {
      return undefined;
    }
    return new LegEndRoute(
      jsonObject.trackPathKeys ? List(jsonObject.trackPathKeys.map((json: any) => TrackPathKey.fromJSON(json))) : List()
    );
  }
}
