import {Coordinate} from "ol/coordinate";

export class PlanFragment {

  constructor(readonly meters: number,
              readonly orientation: number,
              readonly streetIndex: number,
              readonly coordinate: Coordinate) {
  }

}
