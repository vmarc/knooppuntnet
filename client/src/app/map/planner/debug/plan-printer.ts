import {Plan} from "../plan/plan";
import {Printer} from "./printer";
import {PlanNode} from "../../../kpn/api/common/planner/plan-node";
import {PlanFlag} from "../plan/plan-flag";

export class PlanPrinter {

  private out = new Printer();

  plan(plan: Plan): Printer {
    this.out.println(`sourceNode=${this.node(plan.sourceNode)}`);
    this.out.println(`sourceFlag=${this.flag(plan.sourceFlag)}`);
    plan.legs.forEach(leg => {
      this.out.println(`  leg key=${leg.key}`);
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

}
