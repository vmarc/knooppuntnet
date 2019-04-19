import {List} from "immutable";
import Coordinate from "ol/coordinate";
import {PlanNode} from "./plan-node";
import {PlanRoute} from "./plan-route";

export class PlanLeg {

  constructor(readonly featureId: string,
              readonly source: PlanNode,
              readonly sink: PlanNode,
              readonly meters: number,
              readonly routes: List<PlanRoute>) {
  }

  coordinates(): List<Coordinate> {
    return this.routes.flatMap(route => route.coordinates());
  }

}
