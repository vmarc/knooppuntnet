// this file is generated, please do not modify

import { LatLonImpl } from '../lat-lon-impl';
import { PlanCoordinate } from './plan-coordinate';

export interface PlanNode {
  readonly featureId: string;
  readonly nodeId: string;
  readonly nodeName: string;
  readonly coordinate: PlanCoordinate;
  readonly latLon: LatLonImpl;
}
