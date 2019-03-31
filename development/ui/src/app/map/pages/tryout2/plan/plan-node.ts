import Coordinate from 'ol/View';

export class PlanNode {

  constructor(public nodeId: string,
              public nodeName: string,
              public coordinate: Coordinate) {
  }

}
