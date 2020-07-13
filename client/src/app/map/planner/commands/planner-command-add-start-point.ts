import {List} from "immutable";
import {PlanNode} from "../../../kpn/api/common/planner/plan-node";
import {PlannerContext} from "../context/planner-context";
import {Plan} from "../plan/plan";
import {PlanFlag} from "../plan/plan-flag";
import {PlannerCommand} from "./planner-command";

export class PlannerCommandAddStartPoint implements PlannerCommand {

  constructor(private node: PlanNode, private sourceFlag: PlanFlag) {
  }

  public do(context: PlannerContext) {
    const plan = new Plan(this.node, this.sourceFlag, List());
    context.markerLayer.addFlag(this.sourceFlag);
    context.updatePlan(plan);
  }

  public undo(context: PlannerContext) {
    context.updatePlan(Plan.empty);
    context.markerLayer.removeFlag(this.sourceFlag);
  }

}
