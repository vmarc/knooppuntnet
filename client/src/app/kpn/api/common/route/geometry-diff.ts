// this class is generated, please do not modify

import {PointSegment} from './point-segment';

export class GeometryDiff {

  constructor(readonly common: Array<PointSegment>,
              readonly before: Array<PointSegment>,
              readonly after: Array<PointSegment>) {
  }

  public static fromJSON(jsonObject: any): GeometryDiff {
    if (!jsonObject) {
      return undefined;
    }
    return new GeometryDiff(
      jsonObject.common.map((json: any) => PointSegment.fromJSON(json)),
      jsonObject.before.map((json: any) => PointSegment.fromJSON(json)),
      jsonObject.after.map((json: any) => PointSegment.fromJSON(json))
    );
  }
}
