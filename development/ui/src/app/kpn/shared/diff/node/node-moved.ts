// this class is generated, please do not modify

import {LatLonImpl} from '../../lat-lon-impl';

export class NodeMoved {
  readonly before: LatLonImpl;
  readonly after: LatLonImpl;
  readonly distance: number;

  constructor(before: LatLonImpl,
              after: LatLonImpl,
              distance: number) {
    this.before = before;
    this.after = after;
    this.distance = distance;
  }

  public static fromJSON(jsonObject): NodeMoved {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeMoved(
      LatLonImpl.fromJSON(jsonObject.before),
      LatLonImpl.fromJSON(jsonObject.after),
      jsonObject.distance
    );
  }
}
