import {List} from "immutable";
import {PlanLeg} from "./plan-leg";
import {PlanNode} from "./plan-node";

export class Plan {

  private static _empty: Plan = new Plan(null, List());

  private constructor(readonly source: PlanNode,
                      readonly legs: List<PlanLeg>) {
  }

  get sink(): PlanNode {
    const lastLeg = this.legs.last(null);
    if (lastLeg) {
      return lastLeg.sink;
    }
    return this.source;
  }

  cumulativeMetersLeg(legIndex: number): number {
    if (legIndex < this.legs.size) {
      return this.legs.slice(0, legIndex + 1).map(l => l.meters).reduce((sum, current) => sum + current, 0);
    }
    return 0;
  }

  cumulativeKmLeg(legIndex: number): string {
    const meters = this.cumulativeMetersLeg(legIndex);
    const km = Math.round(meters / 100) / 10;
    const kmString = parseFloat(km.toFixed(1));
    return kmString + " km";
  }

  static empty(): Plan {
    return Plan._empty;
  }

  static create(source: PlanNode, legs: List<PlanLeg>): Plan {
    return new Plan(source, legs);
  }

}
