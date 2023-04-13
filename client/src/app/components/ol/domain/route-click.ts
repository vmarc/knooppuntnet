import { RouteFeature } from '@app/planner/domain/features';
import { Coordinate } from 'ol/coordinate';

export class RouteClick {
  constructor(readonly coordinate: Coordinate, readonly route: RouteFeature) {}
}
