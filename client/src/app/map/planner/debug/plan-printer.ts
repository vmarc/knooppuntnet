import {Plan} from "../plan/plan";
import {Printer} from "./printer";
import {PlanNode} from "../../../kpn/api/common/planner/plan-node";
import {PlanFlag} from "../plan/plan-flag";
import {LegEnd} from "../../../kpn/api/common/planner/leg-end";
import {TrackPathKey} from "../../../kpn/api/common/common/track-path-key";
import {List} from "immutable";

export class PlanPrinter {

  private out = new Printer();

  plan(plan: Plan): Printer {
    this.out.println(`sourceNode=${this.node(plan.sourceNode)}`);
    this.out.println(`sourceFlag=${this.flag(plan.sourceFlag)}`);
    plan.legs.forEach(leg => {
      this.out.println(`  leg featureId=${leg.featureId}, key=${leg.key}`);
      this.out.println(`    sink=${PlanPrinter.sink(leg.sink)}`);
      if (leg.viaFlag !== null) {
        this.out.println(`    viaFlag=${this.flag(leg.viaFlag)}`);
      } else {
        this.out.println(`    viaFlag=null`);
      }
      this.out.println(`    sinkFlag=${this.flag(leg.sinkFlag)}`);
      this.out.println(`    sinkNode=${this.node(leg.sinkNode)}`);
      // leg.routes
    });
    return this.out;
  }

  private node(planNode: PlanNode): string {
    return `${planNode.nodeName}/${planNode.nodeId} ${this.out.coordinate(planNode.coordinate)}`;
  }

  private flag(planFlag: PlanFlag): string {
    return `${planFlag.flagType} ${this.out.coordinate(planFlag.coordinate)}`;
  }

  private static sink(legEnd: LegEnd): string {
    if (legEnd.node) {
      return `${legEnd.node.nodeId}`;
    }
    if (legEnd.route && legEnd.route.selection) {
      const selection = PlanPrinter.trackPathKey(legEnd.route.selection);
      const trackPathKeys = PlanPrinter.trackPathKeys(legEnd.route.trackPathKeys);
      return `${trackPathKeys}, selection=${selection}`;
    }
    if (legEnd.route) {
      return PlanPrinter.trackPathKeys(legEnd.route.trackPathKeys);
    }
  }

  private static trackPathKeys(keys: List<TrackPathKey>): string {
    return keys.map(key => PlanPrinter.trackPathKey(key)).join("|");
  }

  private static trackPathKey(key: TrackPathKey): string {
    return `${key.routeId}.${key.pathId}`;
  }

}
