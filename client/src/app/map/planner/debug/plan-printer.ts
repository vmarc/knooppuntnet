import { TrackPathKey } from '@api/common/common/track-path-key';
import { LegEnd } from '@api/common/planner/leg-end';
import { PlanNode } from '@api/common/planner/plan-node';
import { Plan } from '../plan/plan';
import { PlanFlag } from '../plan/plan-flag';
import { Printer } from './printer';

export class PlanPrinter {
  private out = new Printer();

  plan(plan: Plan): Printer {
    this.out.println(`sourceNode=${this.node(plan.sourceNode)}`);
    this.out.println(`sourceFlag=${this.flag(plan.sourceFlag)}`);
    this.out.println(`sinkFlag()=${this.flag(plan.sinkFlag())}`);
    plan.legs.forEach((leg) => {
      this.out.println(`  leg featureId=${leg.featureId}, key=${leg.key}`);
      this.out.println(`    sink=${this.sink(leg.sink)}`);
      if (leg.viaFlag !== null) {
        this.out.println(`    viaFlag=${this.flag(leg.viaFlag)}`);
      } else {
        this.out.println(`    viaFlag=none`);
      }
      this.out.println(`    sinkFlag=${this.flag(leg.sinkFlag)}`);
      this.out.println(`    sinkNode=${this.node(leg.sinkNode)}`);
      // leg.routes
    });
    return this.out;
  }

  private node(planNode: PlanNode): string {
    if (planNode) {
      return `${planNode.nodeName}/${planNode.nodeId} ${this.out.coordinate(
        planNode.coordinate
      )}`;
    }
    return 'none';
  }

  private flag(planFlag: PlanFlag): string {
    if (planFlag) {
      return `${planFlag.flagType} ${this.out.coordinate(planFlag.coordinate)}`;
    }
    return 'none';
  }

  private sink(legEnd: LegEnd): string {
    if (legEnd) {
      if (legEnd.node) {
        return `${legEnd.node.nodeId}`;
      }
      if (legEnd.route && legEnd.route.selection) {
        const selection = this.trackPathKey(legEnd.route.selection);
        const trackPathKeys = this.trackPathKeys(legEnd.route.trackPathKeys);
        return `${trackPathKeys}, selection=${selection}`;
      }
      if (legEnd.route) {
        return this.trackPathKeys(legEnd.route.trackPathKeys);
      }
    }
    return 'none';
  }

  private trackPathKeys(keys: TrackPathKey[]): string {
    return keys.map((key) => this.trackPathKey(key)).join('|');
  }

  private trackPathKey(key: TrackPathKey): string {
    return `${key.routeId}.${key.pathId}`;
  }
}
