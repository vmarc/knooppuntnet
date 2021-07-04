// this class is generated, please do not modify

import { LatLonImpl } from '../lat-lon-impl';

export class PointSegment {
  constructor(readonly p1: LatLonImpl, readonly p2: LatLonImpl) {}

  public static fromJSON(jsonObject: any): PointSegment {
    if (!jsonObject) {
      return undefined;
    }
    return new PointSegment(
      LatLonImpl.fromJSON(jsonObject.p1),
      LatLonImpl.fromJSON(jsonObject.p2)
    );
  }
}
