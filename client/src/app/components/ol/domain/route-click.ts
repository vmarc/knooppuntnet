import { Coordinate } from 'ol/coordinate';
import { RouteFeature } from '../../../map/planner/features/route-feature';

export class RouteClick {
  constructor(readonly coordinate: Coordinate, readonly route: RouteFeature) {}
}
