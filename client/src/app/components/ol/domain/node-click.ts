import { NetworkNodeFeature } from '@app/planner/domain/features/network-node-feature';
import { Coordinate } from 'ol/coordinate';

export class NodeClick {
  constructor(
    readonly coordinate: Coordinate,
    readonly node: NetworkNodeFeature
  ) {}
}
