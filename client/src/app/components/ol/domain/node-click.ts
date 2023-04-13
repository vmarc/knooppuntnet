import { NetworkNodeFeature } from '@app/planner/domain/features';
import { Coordinate } from 'ol/coordinate';

export class NodeClick {
  constructor(
    readonly coordinate: Coordinate,
    readonly node: NetworkNodeFeature
  ) {}
}
