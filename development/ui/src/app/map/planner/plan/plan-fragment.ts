import {Coordinate} from "ol/coordinate";

export class PlanFragment {

  constructor(public readonly meters: number,
              public readonly orientation: number,
              public readonly streetIndex: number,
              public readonly coordinate: Coordinate) {
  }

}
