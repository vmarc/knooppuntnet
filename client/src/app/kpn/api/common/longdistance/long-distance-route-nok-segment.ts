// this class is generated, please do not modify

import {Bounds} from '../bounds';

export class LongDistanceRouteNokSegment {

  constructor(readonly id: number,
              readonly meters: number,
              readonly distance: number,
              readonly bounds: Bounds,
              readonly geoJson: string) {
  }

  public static fromJSON(jsonObject: any): LongDistanceRouteNokSegment {
    if (!jsonObject) {
      return undefined;
    }
    return new LongDistanceRouteNokSegment(
      jsonObject.id,
      jsonObject.meters,
      jsonObject.distance,
      Bounds.fromJSON(jsonObject.bounds),
      jsonObject.geoJson
    );
  }
}
