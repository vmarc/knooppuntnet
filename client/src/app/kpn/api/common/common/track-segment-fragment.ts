// this class is generated, please do not modify

import { TrackPoint } from './track-point';

export class TrackSegmentFragment {
  constructor(
    readonly trackPoint: TrackPoint,
    readonly meters: number,
    readonly orientation: number,
    readonly streetIndex: number
  ) {}

  static fromJSON(jsonObject: any): TrackSegmentFragment {
    if (!jsonObject) {
      return undefined;
    }
    return new TrackSegmentFragment(
      TrackPoint.fromJSON(jsonObject.trackPoint),
      jsonObject.meters,
      jsonObject.orientation,
      jsonObject.streetIndex
    );
  }
}
