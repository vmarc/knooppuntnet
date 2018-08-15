// this class is generated, please do not modify

import {LatLonImpl} from '../lat-lon-impl';

export class WayGeometry {

  constructor(public id?: number,
              public nodes?: Array<LatLonImpl>) {
  }

  public static fromJSON(jsonObject): WayGeometry {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new WayGeometry();
    instance.id = jsonObject.id;
    instance.nodes = jsonObject.nodes ? jsonObject.nodes.map(json => LatLonImpl.fromJSON(json)) : [];
    return instance;
  }
}

