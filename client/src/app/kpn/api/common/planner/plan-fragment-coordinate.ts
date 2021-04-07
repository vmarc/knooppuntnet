// this class is generated, please do not modify

import { LatLonImpl } from '../lat-lon-impl';
import { Coordinate } from 'ol/coordinate';

export class PlanFragmentCoordinate {
  constructor(readonly coordinate: Coordinate, readonly latLon: LatLonImpl) {}

  static fromJSON(jsonObject: any): PlanFragmentCoordinate {
    if (!jsonObject) {
      return undefined;
    }
    return new PlanFragmentCoordinate(
      [jsonObject.coordinate.x, jsonObject.coordinate.y],
      LatLonImpl.fromJSON(jsonObject.latLon)
    );
  }
}
