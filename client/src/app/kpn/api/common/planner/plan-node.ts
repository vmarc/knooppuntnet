// this class is generated, please do not modify

import { LatLonImpl } from '../lat-lon-impl';
import { Coordinate } from 'ol/coordinate';

export class PlanNode {
  constructor(
    readonly featureId: string,
    readonly nodeId: string,
    readonly nodeName: string,
    readonly coordinate: Coordinate,
    readonly latLon: LatLonImpl
  ) {}

  static fromJSON(jsonObject: any): PlanNode {
    if (!jsonObject) {
      return undefined;
    }
    return new PlanNode(
      jsonObject.featureId,
      jsonObject.nodeId,
      jsonObject.nodeName,
      [jsonObject.coordinate.x, jsonObject.coordinate.y],
      LatLonImpl.fromJSON(jsonObject.latLon)
    );
  }
}
