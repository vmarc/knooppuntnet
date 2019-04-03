import Coordinate from 'ol/coordinate';

export class PlannerDragLeg {

  constructor(public readonly oldLegId: string,
              public readonly anchor1: Coordinate,
              public readonly anchor2: Coordinate) {
  }

}
