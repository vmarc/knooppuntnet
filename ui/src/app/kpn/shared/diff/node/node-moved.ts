// this class is generated, please do not modify

import {LatLonImpl} from '../../lat-lon-impl';

export class NodeMoved {

  constructor(public before?: LatLonImpl,
              public after?: LatLonImpl,
              public distance?: number) {
  }

  public static fromJSON(jsonObject): NodeMoved {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NodeMoved();
    instance.before = LatLonImpl.fromJSON(jsonObject.before);
    instance.after = LatLonImpl.fromJSON(jsonObject.after);
    instance.distance = jsonObject.distance;
    return instance;
  }
}

