// this class is generated, please do not modify

import {PointSegment} from './point-segment';

export class GeometryDiff {

  constructor(public common?: Array<PointSegment>,
              public before?: Array<PointSegment>,
              public after?: Array<PointSegment>) {
  }

  public static fromJSON(jsonObject): GeometryDiff {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new GeometryDiff();
    instance.common = jsonObject.common ? jsonObject.common.map(json => PointSegment.fromJSON(json)) : [];
    instance.before = jsonObject.before ? jsonObject.before.map(json => PointSegment.fromJSON(json)) : [];
    instance.after = jsonObject.after ? jsonObject.after.map(json => PointSegment.fromJSON(json)) : [];
    return instance;
  }
}

