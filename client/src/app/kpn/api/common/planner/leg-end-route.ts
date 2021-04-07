// this class is generated, please do not modify

import { TrackPathKey } from '../common/track-path-key';

export class LegEndRoute {
  constructor(
    readonly trackPathKeys: Array<TrackPathKey>,
    readonly selection: TrackPathKey
  ) {}

  public static fromJSON(jsonObject: any): LegEndRoute {
    if (!jsonObject) {
      return undefined;
    }
    return new LegEndRoute(
      jsonObject.trackPathKeys.map((json: any) => TrackPathKey.fromJSON(json)),
      TrackPathKey.fromJSON(jsonObject.selection)
    );
  }
}
