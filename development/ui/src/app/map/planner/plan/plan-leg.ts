import {List} from "immutable";
import Coordinate from "ol/coordinate";
import {PlanNode} from "./plan-node";
import {PlanRoute} from "./plan-route";

export class PlanLeg {

  constructor(public readonly featureId: string,
              public readonly source: PlanNode,
              public readonly sink: PlanNode,
              public readonly meters: number,
              public readonly routes: List<PlanRoute>) {
  }

  coordinates(): List<Coordinate> {
    return this.routes.flatMap(route => route.coordinates());
  }

}
