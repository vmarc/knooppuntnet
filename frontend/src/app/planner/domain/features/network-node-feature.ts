import { PlanNode } from '@api/common/planner';
import { Coordinate } from 'ol/coordinate';
import { PlanUtil } from '../plan/plan-util';
import { MapFeature } from './map-feature';

export class NetworkNodeFeature extends MapFeature {
  constructor(
    readonly node: PlanNode,
    public readonly proposed: boolean
  ) {
    super();
  }

  static create(
    nodeId: string,
    nodeName: string,
    nodeLongName: string,
    coordinate: Coordinate,
    proposed: boolean
  ) {
    return new NetworkNodeFeature(
      PlanUtil.planNodeWithCoordinate(
        nodeId,
        nodeName,
        nodeLongName,
        coordinate
      ),
      proposed
    );
  }
}
