import {List} from "immutable";
import {Coordinate} from 'ol/coordinate';
import {PlanLegFragment} from "./plan-leg-fragment";
import {PlanNode} from "./plan-node";

export class PlanLeg {

  constructor(public readonly featureId: string,
              public readonly source: PlanNode,
              public readonly sink: PlanNode,
              public readonly fragments: List<PlanLegFragment>) {
  }

  meters(): number {
    return this.fragments.map(f => f.meters).reduce((sum, current) => sum + current, 0);
  }

  coordinates(): List<Coordinate> {
    return this.fragments.flatMap(f => f.coordinates);
  }

}
