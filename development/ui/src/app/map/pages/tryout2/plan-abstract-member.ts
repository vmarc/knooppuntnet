import {PlanNode} from "./plan-node";
import {PlanLeg} from "./plan-leg";
import {PlanMember} from "./plan-member";

export abstract class PlanAbstractMember implements PlanMember {

  isUserSelectedNode(): boolean {
    return false;
  }

  isServerPlannedNode(): boolean {
    return false;
  }

  isLeg(): boolean {
    return false;
  }

  toNode(): PlanNode {
    throw new Error("Illegal operation");
  }

  toLeg(): PlanLeg {
    throw new Error("Illegal operation");
  }
}
