import { Coordinate } from '@api/custom/coordinate';

export class PlannerDragLeg {
  constructor(
    readonly oldLegId: string,
    readonly anchor1: Coordinate,
    readonly anchor2: Coordinate
  ) {}
}
