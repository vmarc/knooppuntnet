// this file is generated, please do not modify

import { LatLonImpl } from '../lat-lon-impl';
import { PlanCoordinate } from './plan-coordinate';

export interface PlanFragment {
  readonly meters: number;
  readonly orientation: number;
  readonly streetIndex: number;
  readonly coordinate: PlanCoordinate;
  readonly latLon: LatLonImpl;
}
