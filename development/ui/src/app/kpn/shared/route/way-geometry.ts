// this class is generated, please do not modify

import {List} from 'immutable';
import {LatLonImpl} from '../lat-lon-impl';

export class WayGeometry {
  readonly id: number;
  readonly nodes: List<LatLonImpl>;

  constructor(id: number,
              nodes: List<LatLonImpl>) {
    this.id = id;
    this.nodes = nodes;
  }

  public static fromJSON(jsonObject): WayGeometry {
    if (!jsonObject) {
      return undefined;
    }
    return new WayGeometry(
      jsonObject.id,
      jsonObject.nodes ? List(jsonObject.nodes.map(json => LatLonImpl.fromJSON(json))) : List()
    );
  }
}
