import {PlanLeg} from "./plan-leg";
import {PlanNode} from "./plan-node";

export interface PlanMember {

  isUserSelectedNode(): boolean;

  isServerPlannedNode(): boolean;

  isLeg(): boolean;

  toNode(): PlanNode;

  toLeg(): PlanLeg;
}
