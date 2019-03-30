import Coordinate from 'ol/View';

export class PlannerDragLeg {

  constructor(public readonly legId: string,
              public readonly anchor1: Coordinate,
              public readonly anchor2: Coordinate) {
  }

}
