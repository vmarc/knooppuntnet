// this class is generated, please do not modify

import {LatLonImpl} from '../lat-lon-impl';

export class PointSegment {

  constructor(public p1?: LatLonImpl,
              public p2?: LatLonImpl) {
  }

  public static fromJSON(jsonObject): PointSegment {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new PointSegment();
    instance.p1 = LatLonImpl.fromJSON(jsonObject.p1);
    instance.p2 = LatLonImpl.fromJSON(jsonObject.p2);
    return instance;
  }
}

