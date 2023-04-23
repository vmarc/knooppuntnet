import { PlanNode } from '@api/common/planner';
import { Util } from '@app/components/shared';
import { List } from 'immutable';
import { PlanFlag } from './plan-flag';
import { PlanLeg } from './plan-leg';

export class Plan {
  static readonly empty = new Plan(null, null, List());

  constructor(
    readonly sourceNode: PlanNode,
    readonly sourceFlag: PlanFlag,
    readonly legs: List<PlanLeg>
  ) {}

  withLegs(legs: List<PlanLeg>): Plan {
    return new Plan(this.sourceNode, this.sourceFlag, legs);
  }

  sinkNode(): PlanNode {
    const lastLeg = this.legs.last(null);
    if (lastLeg) {
      return lastLeg.sinkNode;
    }
    return this.sourceNode;
  }

  sinkFlag(): PlanFlag {
    const lastLeg = this.legs.last(null);
    if (lastLeg) {
      return lastLeg.sinkFlag;
    }
    return null;
  }

  meters(): number {
    return Util.sum(this.legs.map((l) => l.meters()));
  }

  cumulativeMetersLeg(legIndex: number): number {
    if (legIndex < this.legs.size) {
      return Util.sum(this.legs.slice(0, legIndex + 1).map((l) => l.meters()));
    }
    return 0;
  }

  cumulativeKmLeg(legIndex: number): string {
    const meters = this.cumulativeMetersLeg(legIndex);
    const km = Math.round(meters / 100) / 10;
    const kmString = parseFloat(km.toFixed(1));
    return kmString + ' km';
  }

  unpavedPercentage(): string {
    const distances: List<number> = this.legs.flatMap((leg) =>
      leg.routes.flatMap((route) =>
        route.segments
          .filter((segment) => segment.surface === 'unpaved')
          .map((segment) => segment.meters)
      )
    );
    const unpavedMeters = Util.sum(distances);
    const percentage = Math.round((100 * unpavedMeters) / this.meters());
    return `${percentage}%`;
  }
}
