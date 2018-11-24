// this class is generated, please do not modify

import {LatLonImpl} from '../lat-lon-impl';

export class PointSegment {
  readonly p1: LatLonImpl;
  readonly p2: LatLonImpl;

  constructor(p1: LatLonImpl,
              p2: LatLonImpl) {
    this.p1 = p1;
    this.p2 = p2;
  }

  public static fromJSON(jsonObject): PointSegment {
    if (!jsonObject) {
      return undefined;
    }
    return new PointSegment(
      LatLonImpl.fromJSON(jsonObject.p1),
      LatLonImpl.fromJSON(jsonObject.p2)
    );
  }
}
