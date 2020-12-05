// this class is generated, please do not modify

import {Bounds} from '../bounds';

export class LongDistanceRouteSegment {

  constructor(readonly id: number,
              readonly meters: number,
              readonly bounds: Bounds,
              readonly geoJson: string) {
  }

  public static fromJSON(jsonObject: any): LongDistanceRouteSegment {
    if (!jsonObject) {
      return undefined;
    }
    return new LongDistanceRouteSegment(
      jsonObject.id,
      jsonObject.meters,
      Bounds.fromJSON(jsonObject.bounds),
      jsonObject.geoJson
    );
  }
}
