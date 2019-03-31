import {PlanNode} from "./plan-node";
import {PlanLeg} from "./plan-leg";

export interface PlanMember {

  isUserSelectedNode(): boolean;

  isServerPlannedNode(): boolean;

  isLeg(): boolean;

  toNode(): PlanNode;

  toLeg(): PlanLeg;
}
