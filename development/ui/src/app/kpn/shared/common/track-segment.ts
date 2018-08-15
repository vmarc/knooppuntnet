// this class is generated, please do not modify

import {TrackPoint} from './track-point';

export class TrackSegment {

  constructor(public trackPoints?: Array<TrackPoint>) {
  }

  public static fromJSON(jsonObject): TrackSegment {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new TrackSegment();
    instance.trackPoints = jsonObject.trackPoints ? jsonObject.trackPoints.map(json => TrackPoint.fromJSON(json)) : [];
    return instance;
  }
}

