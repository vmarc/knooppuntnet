import { Coordinate } from '@api/custom/coordinate';
import { RouteFeature } from '../../features/route-feature';

export class RouteClick {
  constructor(
    readonly coordinate: Coordinate,
    readonly route: RouteFeature
  ) {}
}
