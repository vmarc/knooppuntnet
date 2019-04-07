import Coordinate from 'ol/coordinate';

export class PlanNode {

  constructor(public nodeId: string,
              public nodeName: string,
              public coordinate: Coordinate) {
  }

}
