// this file is generated, please do not modify

import { LatLonImpl } from '../lat-lon-impl';
import { Coordinate } from 'ol/coordinate';

export interface PlanNode {
  readonly featureId: string;
  readonly nodeId: string;
  readonly nodeName: string;
  readonly nodeLongName: string;
  readonly coordinate: Coordinate;
  readonly latLon: LatLonImpl;
}
