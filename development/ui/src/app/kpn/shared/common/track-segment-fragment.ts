// this class is generated, please do not modify

import {List} from 'immutable';
import {TrackPoint} from './track-point';

export class TrackSegmentFragment {

  constructor(readonly surface: string,
              readonly trackPoints: List<TrackPoint>) {
  }

  public static fromJSON(jsonObject): TrackSegmentFragment {
    if (!jsonObject) {
      return undefined;
    }
    return new TrackSegmentFragment(
      jsonObject.surface,
      jsonObject.trackPoints ? List(jsonObject.trackPoints.map(json => TrackPoint.fromJSON(json))) : List()
    );
  }
}
