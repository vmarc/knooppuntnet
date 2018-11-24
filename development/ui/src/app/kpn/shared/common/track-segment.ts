// this class is generated, please do not modify

import {List} from 'immutable';
import {TrackPoint} from './track-point';

export class TrackSegment {
  readonly surface: string;
  readonly trackPoints: List<TrackPoint>;

  constructor(surface: string,
              trackPoints: List<TrackPoint>) {
    this.surface = surface;
    this.trackPoints = trackPoints;
  }

  public static fromJSON(jsonObject): TrackSegment {
    if (!jsonObject) {
      return undefined;
    }
    return new TrackSegment(
      jsonObject.surface,
      jsonObject.trackPoints ? List(jsonObject.trackPoints.map(json => TrackPoint.fromJSON(json))) : List()
    );
  }
}
