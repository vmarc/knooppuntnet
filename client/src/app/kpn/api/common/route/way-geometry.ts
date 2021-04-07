// this class is generated, please do not modify

import { LatLonImpl } from '../lat-lon-impl';

export class WayGeometry {
  constructor(readonly id: number, readonly nodes: Array<LatLonImpl>) {}

  static fromJSON(jsonObject: any): WayGeometry {
    if (!jsonObject) {
      return undefined;
    }
    return new WayGeometry(
      jsonObject.id,
      jsonObject.nodes.map((json: any) => LatLonImpl.fromJSON(json))
    );
  }
}
