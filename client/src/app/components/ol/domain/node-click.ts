import {Coordinate} from 'ol/coordinate';
import {NetworkNodeFeature} from '../../../map/planner/features/network-node-feature';

export class NodeClick {
  constructor(readonly coordinate: Coordinate,
              readonly node: NetworkNodeFeature) {
  }
}
