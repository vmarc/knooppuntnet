// this class is generated, please do not modify

import { TrackPoint } from './track-point';
import { TrackSegmentFragment } from './track-segment-fragment';

export class TrackSegment {
  constructor(
    readonly surface: string,
    readonly source: TrackPoint,
    readonly fragments: Array<TrackSegmentFragment>
  ) {}

  public static fromJSON(jsonObject: any): TrackSegment {
    if (!jsonObject) {
      return undefined;
    }
    return new TrackSegment(
      jsonObject.surface,
      TrackPoint.fromJSON(jsonObject.source),
      jsonObject.fragments.map((json: any) =>
        TrackSegmentFragment.fromJSON(json)
      )
    );
  }
}
