// this file is generated, please do not modify

import { LatLonImpl } from '@api/common';
import { Coordinate } from 'ol/coordinate';

export interface PlanNode {
  readonly featureId: string;
  readonly nodeId: string;
  readonly nodeName: string;
  readonly nodeLongName: string;
  readonly coordinate: Coordinate;
  readonly latLon: LatLonImpl;
}
