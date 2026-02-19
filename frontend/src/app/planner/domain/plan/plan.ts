import { PlanNode } from '@api/common/planner/plan-node';
import { Util } from '@app/shared/components/util';
import { PlanFlag } from './plan-flag';
import { PlanLeg } from './plan-leg';

export class Plan {
  static readonly empty = new Plan(null, null, []);

  constructor(
    readonly sourceNode: PlanNode,
    readonly sourceFlag: PlanFlag,
    readonly legs: ReadonlyArray<PlanLeg>
  ) {}

  withLegs(legs: ReadonlyArray<PlanLeg>): Plan {
    return new Plan(this.sourceNode, this.sourceFlag, legs);
  }

  sinkNode(): PlanNode {
    const lastLeg = this.legs.at(-1);
    return lastLeg ? lastLeg.sinkNode : this.sourceNode;
  }

  sinkFlag(): PlanFlag {
    const lastLeg = this.legs.at(-1);
    return lastLeg ? lastLeg.sinkFlag : null;
  }

  meters(): number {
    return Util.sum(this.legs.map((l) => l.meters()));
  }

  cumulativeMetersLeg(legIndex: number): number {
    if (legIndex < this.legs.length) {
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
    const distances: number[] = this.legs.flatMap((leg) =>
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
